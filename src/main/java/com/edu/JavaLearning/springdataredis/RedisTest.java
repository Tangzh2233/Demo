package com.edu.JavaLearning.springdataredis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

import java.util.*;

/**
 * Created by Administrator on 2017/8/17.
 */
public class RedisTest {
    private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    private static final int VOTE_SCORE = 432;
    private static final int ARTICLES_PER_PAGE = 25;
    public static void main(String[] args) {

        Jedis jedis = RedisUtil.getJedis();
        String article = postArticle(jedis,"每日一笑","tang","https://www.baidu.com");
        System.out.println(article);
    }

    //新建一篇文章
    public static String postArticle(Jedis comm,String title,String user,String link){
        String articleId = String.valueOf(comm.incr("article:"));
        //将作者自己加入到已投票的set中
        String voted = "voted:" + articleId;
        comm.sadd(voted,user);

        //组合文章有序集合的key
        Long now = System.currentTimeMillis() /1000;
        String article = "article:"+articleId;
        HashMap<String,String> articleData = new HashMap<>();
        articleData.put("title",title);
        articleData.put("link",link);
        articleData.put("poster",user);
        articleData.put("time",String.valueOf(now));
        articleData.put("votes","1");
        comm.hmset(article,articleData);
        //根据发布时间和评分排序文章的有序集合(因为涉及score的改变，所以将时间和评分放在score的位置)
        comm.zadd("score:",now + VOTE_SCORE,article);
        comm.zadd("time:",now,article);

        return articleId;
    }

    //获得相差page*25 分数的所有文章的具体内容
    public static List<Map<String,String>> getArticles(Jedis jedis, int page,String type){
        int start = (page-1) * ARTICLES_PER_PAGE;
        int end = start + ARTICLES_PER_PAGE - 1;
        //查出相差page*25 分数的所有文章
        Set<String> articleIds = jedis.zrange(type,start,end);

        List<Map<String,String>> articles = new ArrayList<>();
        for (String articleId:articleIds) {
            //根据文章id查出 具体文章
            Map<String, String> article = jedis.hgetAll(articleId);
            article.put("id",articleId);
            articles.add(article);
        }
        return articles;
    }

    //文章投票
    public static void articleVote(Jedis jedis,String user,String article){
        long cutoff = (System.currentTimeMillis()/1000) - ONE_WEEK_IN_SECONDS;
        //判断文章发布时间是否在目前的一周内
        if(jedis.zscore("time:",article) < cutoff){
            return;
        }
        String articleId = article.substring(article.indexOf(':')-1);
        //更新此文章的已投票人的set
        if(jedis.sadd("vote:"+articleId,user) ==1){
            //更新评分的zset集合
            jedis.zincrby("score:",VOTE_SCORE,article);
            //更新此文章的hash散列中的vote字段值
            jedis.hincrBy(article,"vote",1);
        }
    }

    //文章分组---1
    //把文章添加到分组---1.1
    public static void add_group_Articles(Jedis jedis,String[] articlesId,String groupname){

        for(String article:articlesId){
            String articles = "article:"+article;
            jedis.sadd("group:"+groupname,articles);
        }
    }
    //从某一分组中的文章---1.2
    //此段代码 目前看的不太懂  8-21
    public static List<Map<String,String>> get_group_articles(Jedis jedis,String groupname,int page,String order){
        String key = order + groupname;
        if(!jedis.exists(key)){
            ZParams zParams =  new ZParams().aggregate(ZParams.Aggregate.MAX);
            jedis.zinterstore(key,zParams,"group:"+groupname,order);
            jedis.expire(key,60);
        }
        return getArticles(jedis,page,key);
    }

    //
    private void printArticles(List<Map<String,String>> articles){
        for (Map<String,String> article:articles) {
            System.out.println("id:"+article.get("id"));
            for(Map.Entry<String,String> entry:article.entrySet()){
                if(entry.getKey().equals("id")){
                    continue;
                }
                System.out.println("  "+entry.getKey()+":"+entry.getValue());
            }
        }
    }

}
