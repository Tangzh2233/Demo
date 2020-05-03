package com.edu.JavaLearning.collection;

import com.alibaba.fastjson.JSON;
import com.edu.dao.domain.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/28 6:56 PM
 **/
public class Lambda {


    public static void main(String[] args) {
        map();
        collectors();
        collectorsGroupBy();
    }

    private static void map(){
        List<User> initData = initData();
        List<User> initData1 = initData();
        List<User> initData2 = initData();
        Stream<List<User>> initStream = Stream.of(initData, initData1, initData2);
        int i = initStream.flatMapToInt(users -> users.stream().mapToInt(user -> Integer.valueOf(user.getUserNo())))
                //过滤,符合条件的进入下游
                .filter(item -> item % 2 == 0)
                //去重
                .distinct()
                //各种转化
                .map(item -> item = item * 2)
                //弹出,执行操作,和forEach类似,区别在于他依旧返回Stream流
                .peek(System.out::println)
                //限流操作,此处代表只从Stream流中取3个值
                .limit(3)
                //查找操作
                .findAny()
                .orElse(-1);

        int reduce = initStream.flatMapToInt(users -> users.stream().mapToInt(user -> Integer.valueOf(user.getUserNo())))
                .filter(item -> item % 2 == 0)
                .distinct()
                .map(item -> item = item * 2)
                .peek(System.out::println)
                //规约操作,按照规则将Stream流约束成一个值
//              .reduce(Integer.MAX_VALUE,Integer::min);
                .reduce(0, (a, b) -> a + b);
                //anyMatch Stream 中只要有一个元素符合传入的 predicate，返回 true
                //allMatch Stream 中全部元素符合传入的 predicate，返回 true
                //noneMatch Stream 中没有一个元素符合传入的 predicate，返回 true
//              .anyMatch(item -> item > 10010);
        System.out.println(i);


        List<Integer> collect = initData.stream().map(User::getId)
                .distinct()
                .collect(Collectors.toList());
    }

    public static void optional(String text) {
        //text不为空则打印
        Optional.ofNullable(text).ifPresent(System.out::print);
        //text不为空则返回字符串长度,否则返回-1
        Integer textLength = Optional.ofNullable(text).map(String::length).orElse(-1);
    }

    public static void collectorsGroupBy() {
        List<User> initData = initData();
        //key-> data.getDate.getNano value->[User::getUserNo]
        //依据User.date 分成若干组
        Map<Long, List<String>> listMap = initData.stream()
                .collect(Collectors.groupingBy(item -> item.getDate().toInstant(ZoneOffset.of("+8")).toEpochMilli(), Collectors.mapping(User::getUserNo, Collectors.toList())));
        System.out.println(JSON.toJSONString(listMap));

        //分组计数,每组的数量
        Map<Long, Long> countMap = initData.stream()
                .collect(Collectors.groupingBy(item -> item.getDate().toInstant(ZoneOffset.of("+8")).toEpochMilli(), Collectors.counting()));
        System.out.println(JSON.toJSONString(countMap));


        initData.stream().sorted(User::compareTo)
                .forEachOrdered(System.out::println);
        //依据User.date分组,分组求User.userNo的和
        Map<Long, Integer> sumMap = initData.stream()
                .collect(Collectors.groupingBy(item -> item.getDate().toInstant(ZoneOffset.of("+8")).toEpochMilli(), Collectors.summingInt(item -> Integer.valueOf(item.getUserNo()))));
        System.out.println(JSON.toJSONString(sumMap));

        //List<User>按照date分组
        Map<Long, List<User>> listMapUser = initData.stream()
                .collect(Collectors.groupingBy(item -> item.getDate().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        System.out.println(listMapUser);

        //按找date分组,取平均值放入TreeMap中
        TreeMap<Long, Double> treeMap = initData.stream()
                .collect(Collectors.groupingBy(a -> a.getDate().toInstant(ZoneOffset.of("+8")).toEpochMilli(), TreeMap::new, Collectors.averagingInt(b -> Integer.valueOf(b.getPassword()))));
        System.out.println(JSON.toJSONString(treeMap));

        //输出最大的id
        initData.stream()
                .mapToInt(User::getId)
                .max()
                .ifPresent(System.out::print);
        //输出最小id的User对象
        initData.stream()
                .min(Comparator.comparing(User::getId))
                .ifPresent(System.out::print);

        //Collectors.partitioningBy依据'a -> a.getId() > 1004'条件将Stream分成两份。再将每份依据date做聚合
        //将Stream依据条件分成两份 false -> {} true -> {}
        Map<Boolean, List<LocalDateTime>> booleanListMap = initData.stream()
                .collect(Collectors.partitioningBy(a -> a.getId() > 1004, Collectors.mapping(User::getDate, Collectors.toList())));

        //将List<User>流按照date -> userName 映射Map
        //当发生冲突是执行 (a,b) -> a 只保留第一个
        //              (a,b) -> b 只保留最后一个
        //              (a,b) -> a + "," + b 对已存在字段进行,分隔的累加
        Map<LocalDateTime, String> collect = initData.stream()
                .collect(Collectors.toMap(User::getDate,User::getUsername,(a,b) -> b));

        Map<LocalDateTime, String> collect2 = initData.stream()
                .collect(Collectors.toMap(User::getDate,User::getUsername,(a,b) -> a));

        Map<LocalDateTime, String> collect3 = initData.stream()
                .collect(Collectors.toMap(User::getDate,User::getUsername,(a,b) -> a + "," + b));
        // date -> User 映射。已存在则不插入。类似 putIfAbsent()
        Map<LocalDateTime, User> collect1 = initData.stream()
                .collect(Collectors.toMap(User::getDate, Function.identity(), (a, b) -> a));

        System.out.println("=======end=========="+collect+collect2+collect3);
    }

    public static void collectors(){
        List<User> initData = initData();
        //Collectors.averagingInt
        //password的平均值
        Double average = initData.stream()
                .collect(Collectors.averagingInt(item -> Integer.valueOf(item.getPassword())));
        System.out.println(average);

        //password求和
        Integer summing = initData.stream().mapToInt(a -> Integer.valueOf(a.getPassword())).sum();
        System.out.println(summing);

        //userName聚合
        String join = initData.stream()
                .map(User::getUsername)
                .distinct()
                .limit(5)
                .collect(Collectors.joining("||", "======", "======="));
        System.out.println(join);

        //collectingAndThen 收集到然后-> 执行 "'then+'" + a"。对收集的结果再做处理
        String collectThen = initData.stream()
                .map(User::getUsername)
                .limit(10)
                .collect(Collectors.collectingAndThen(Collectors.toList(), a -> "then+" + a));
        System.out.println(collectThen);
        //password的平均值,然后按照 'average password: ${score}'输出
        String collectThenAverage = initData.stream()
                .collect(Collectors.collectingAndThen(Collectors.averagingInt(a -> Integer.valueOf(a.getPassword())), b -> "average password:" + b));
        System.out.println(collectThenAverage);
    }

    private static List<User> initData(){
        List<User> data = new ArrayList<>();
        for(int i = 0; i< 5 ;i++){
            LocalDateTime now = LocalDateTime.now();
            data.add(new User(i,"Tangzh" + i,"123",now,"100" + i));
        }
        for(int i = 0; i< 5 ;i++){
            LocalDateTime now = LocalDateTime.now();
            data.add(new User(i,"Linzh" + i,"456",now,"100" + i * 2));
        }
        for(int i = 0; i< 5 ;i++){
            LocalDateTime now = LocalDateTime.now();
            data.add(new User(i,"Mmbk" + i,"789",now,"100" + i * 3));
        }
        for(int i = 0; i< 5 ;i++){
            LocalDateTime now = LocalDateTime.now();
            data.add(new User(i,"Yuhs" + i,"1011",now,"100" + i * 4));
        }
        for(int i = 0; i< 5 ;i++){
            LocalDateTime now = LocalDateTime.now();
            data.add(new User(i,"Dog" + i,"1213",now,"100" + i * 5));
        }
        return data;
    }

    public static Date localDateTimeToDate(LocalDateTime date) {
        return Date.from(date.toInstant(ZoneOffset.of("+8")));
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }
}
