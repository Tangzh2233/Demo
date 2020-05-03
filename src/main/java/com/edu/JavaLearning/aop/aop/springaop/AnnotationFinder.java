package com.edu.JavaLearning.aop.aop.springaop;

import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/12/16 5:23 PM
 * 切面注解获取工具类
 **/
public class AnnotationFinder {

    /**
     * url -> @CheckLogin 的缓存
     * <p>
     * url -> method -> t.simpleClassName -> annotation
     */
    private final static ConcurrentHashMap<String, Object> CACHE_MAP = new ConcurrentHashMap<>();

    private final static Object NONE = new Object();

    /**
     * 查询当前joinPoint是否存在t注解
     *
     * @param joinPoint
     * @param request
     * @param t
     * @return
     */
    public static <T extends Annotation> T find(ProceedingJoinPoint joinPoint, HttpServletRequest request, Class<T> t) {
        String key = t.getSimpleName() + "_" + request.getRequestURI();
        Object value = CACHE_MAP.get(key);
        if (value != null && value != NONE) {
            return (T) value;
        }
        try {
            Method method = findMethod(joinPoint);
            T annotation = AnnotationUtils.findAnnotation(method, t);
            if (annotation == null) {
                annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), t);
            }
            CACHE_MAP.putIfAbsent(key, ObjectUtils.defaultIfNull(annotation, NONE));
            // 避免恶意URL访问
            if (CACHE_MAP.size() >= 4096) {
                CACHE_MAP.clear();
            }
            return annotation;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

    }

    private static Method findMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        return target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
    }

    static class None {
    }

}
