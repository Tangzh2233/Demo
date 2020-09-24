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
import java.util.Date;
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

    /**
     * object中的所有注解生效
     * @param object
     * @param groups
     * @param <T>
     */
    public static <T> void check(T object,Class<?>... groups){
        Set<ConstraintViolation<T>> validate = validator.validate(object, groups);
        collectException(validate);
    }

    /**
     * 单独校验 object中的某个paramName
     * @param object
     * @param paramName
     * @param groups
     * @param <T>
     */
    public static <T> void checkParam(T object,String paramName,Class<?>... groups){
        Set<ConstraintViolation<T>> validate = validator.validateProperty(object,paramName, groups);
        collectException(validate);
    }

    private static  <T> void collectException(Set<ConstraintViolation<T>> violationSet){
        if(violationSet.isEmpty()){
            return;
        }
        HashMap<String, String> map = new HashMap<>(violationSet.size());
        violationSet.forEach(item -> map.put(item.getPropertyPath().toString(),item.getMessage()));
        log.error("参数校验失败 {}", JSON.toJSONString(map));
        throw new IllegalArgumentException(JSON.toJSONString(map));
    }

    public static void main(String[] args) {
        User user = new User();
        user.setId(1001);
        user.setPassword("");
        System.out.println(user.toString());
        ParamsCheckUtil.checkParam(user,"password");
//        paramsCheck("");
    }

    public static void paramsCheck(@NotBlank(message = "userId不可为空") String userId){
        ParamsCheckUtil.check(userId);
    }
}
