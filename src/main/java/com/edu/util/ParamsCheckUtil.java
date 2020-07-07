package com.edu.util;

import com.alibaba.fastjson.JSON;
import com.edu.dao.domain.User;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Set;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/8 11:25 AM
 **/
public class ParamsCheckUtil {

    private final static Logger log = LoggerFactory.getLogger(ParamsCheckUtil.class);

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static <T> void check(T object,Class<?>... groups){
        Set<ConstraintViolation<T>> validate = validator.validate(object, groups);
        if(validate.isEmpty()){
            return;
        }
        HashMap<String, String> map = new HashMap<>(validate.size());
        validate.forEach(item -> map.put(item.getPropertyPath().toString(),item.getMessage()));
        log.error("参数校验失败 {}", JSON.toJSONString(map));
        throw new IllegalArgumentException(JSON.toJSONString(map));
    }

    public static void main(String[] args) {
        User user = new User();
        user.setId(1001);
        user.setPassword("23232333");
        System.out.println(user.toString());
//        ParamsCheckUtil.check(user);
        paramsCheck("");
    }

    public static void paramsCheck(@NotBlank(message = "userId不可为空") String userId){
        ParamsCheckUtil.check(userId);
    }
}
