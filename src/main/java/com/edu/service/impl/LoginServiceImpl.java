package com.edu.service.impl;

import com.alibaba.fastjson.JSON;
import com.edu.common.Assert;
import com.edu.common.Constants;
import com.edu.common.UUIDUtil;
import com.edu.common.result.ResultData;
import com.edu.dao.domain.User;
import com.edu.dao.mapper.ideaDemo.DlogMapper;
import com.edu.dao.mapper.ideaDemo.UserMapper;
import com.edu.service.ILoginService;
import com.edu.util.CookieUtils;
import com.edu.util.RedisUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/9/14.
 */
@Service
public class LoginServiceImpl implements ILoginService {

    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Value("${redis.key.expire.time}")
    private Integer key_expire_time;
    @Value("${redis.session.key}")
    private String user_session_key;

    @Override
    public ResultData userLogin(String name, String pwd, HttpServletRequest request, HttpServletResponse response) {
        User user = userMapper.getUserByName(name);
        String token = UUIDUtil.getUUID().substring(10);
        if (user == null) {
            return ResultData.defaultFail("用户信息有误!");
        }
        if (!(DigestUtils.md5DigestAsHex(pwd.getBytes())).equals(user.getPassword())) {
            return ResultData.defaultFail("用户信息有误!");
        }
        user.setPassword(null);

        //设置token+cookie缓存
        RedisUtil.set(user_session_key + ":" + token, JSON.toJSONString(user), key_expire_time);
        CookieUtils.setCookie(request, response, Constants.USER_TOKEN, token);

        return ResultData.defaultSuccess();
    }

    @Override
    public ResultData userRegister(User user) {
        Assert.isEmpty(user.getUsername(), "参数不能为空");
        Assert.isEmpty(user.getPassword(), "参数不能为空");
        final User usr = new User();
        usr.setUsername(user.getUsername());
        usr.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        int i = userMapper.insertUserList(Lists.newArrayList(usr));
        if (i > 0) {
            return ResultData.defaultSuccess();
        } else {
            return ResultData.defaultFail();
        }
    }

    @Override
    public ResultData loginOut(HttpServletRequest request, HttpServletResponse response, String token) {
        try {
            String json = RedisUtil.get(user_session_key + ":" + token);
            if (StringUtils.isBlank(json)) {
                return ResultData.defaultSuccess("session已过期");
            }
            RedisUtil.del(user_session_key + ":" + token);
            CookieUtils.deleteCookie(request, response, Constants.USER_TOKEN);
            return ResultData.defaultSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.defaultSuccess();
        }
    }

    @Override
    public ResultData checkToken(String token) {
        String json = RedisUtil.get(user_session_key + ":" + token);
        if (StringUtils.isBlank(json)) {
            return ResultData.defaultFail("身份已过期");
        }
        //更新redis key 时间
        RedisUtil.expire(user_session_key + ":" + token, key_expire_time);
        return ResultData.defaultSuccess(JSON.parseObject(json, User.class));
    }

}
