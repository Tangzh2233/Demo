package com.edu.JavaLearning.aop.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.edu.JavaLearning.aop.aop.annotation.*;
import com.edu.util.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author linzhihao
 * @date 2018/5/3
 */
@Aspect
public class CatLogAspect {

    private ThreadLocal<Stack<Transaction>> transactionThreadLocal = new ThreadLocal<>();

    private Map<String, Field> cacheField = new HashMap<>();


    @Before("execution(* *(..)) && @annotation(logTransaction)")
    public void newTransaction(LogTransaction logTransaction) {
        Transaction transaction = Cat.newTransaction(logTransaction.type(), logTransaction.name());
        Stack<Transaction> transactionStack = transactionThreadLocal.get();
        if (transactionStack == null) {
            transactionStack = new Stack<>();
            transactionThreadLocal.set(transactionStack);
        }
        transaction.setStatus(Message.SUCCESS);
        transactionStack.push(transaction);
    }

    @Before("execution(* *(..)) && @annotation(logMetricCount)")
    public void logMetricCount(LogMetricCount logMetricCount) {
        for (String metric : logMetricCount.metricKey()) {
            Cat.logMetricForCount(metric);
        }
    }

    /**
     * 表达式execution(* *(..)) && @annotation(logMetricSum)和@annotation(logMetricSum)
     * 其实是一样的，但是ajcBug导致只定义表达式@annotation(logMetricSum)的时候，如果调用标注注解的方法
     * 会导致方法的切面执行两次。
     *
     * @param point        连接点
     * @param logMetricSum 注解对象
     */
    @Before("execution(* *(..)) && @annotation(logMetricSum)")
    public void logMetricSum(JoinPoint point, LogMetricSum logMetricSum) {
        Map<String, Object> values = getValueFromParams(point.getArgs(), logMetricSum.metric());
        if (values.isEmpty()) {
            Cat.logError(new NoSuchFieldException("MetricSum:请求参数中没有找到，注解配置中的对应的属性！"));
        } else {
            for (int i = 0; i < logMetricSum.metric().length; i++) {
                try {
                    Cat.logMetricForSum(logMetricSum.metricKey()[i], Double.valueOf(values.get(logMetricSum.metric()[i]).toString()));
                } catch (NumberFormatException e) {
                    Cat.logError("属性值必须是数字类型的字符串", e);
                }
            }
        }

    }

    @Before("execution(* *(..)) && @annotation(logMetricDuration)")
    public void logMetricDuration(JoinPoint point, LogMetricDuration logMetricDuration) {
        Map<String, Object> values = getValueFromParams(point.getArgs(), logMetricDuration.name());
        if (values.isEmpty()) {
            Cat.logError(new NoSuchFieldException("MetricDuration:请求参数中没有找到，注解配置中的对应的属性！"));
        } else {
            for (int i = 0; i < logMetricDuration.name().length; i++) {
                try {
                    Cat.logMetricForDuration(logMetricDuration.metricName()[i], Long.valueOf(values.get(logMetricDuration.name()[i]).toString()));
                } catch (NumberFormatException e) {
                    Cat.logError("属性值必须是数字类型的字符串", e);
                }
            }
        }
    }

    @Before("execution(* *(..)) && @annotation(logEventNoResult)")
    public void logEventNoResult(LogEventNoResult logEventNoResult) {
        if (!"".equals(logEventNoResult.type()) && !"".equals(logEventNoResult.name())) {
            Cat.logEvent(logEventNoResult.type(), logEventNoResult.name());
        }
    }

    /**
     * LogEvent通过标注该注解的方法返回结果来进行打点记录，
     * 如果方法没有返回结果可以考虑直接使用Cat.logEvent()这个api来进行打点
     * 或者使用注解LogEventNoResult来进行标注
     *
     * @param logEvent 注解对象
     * @param result   要求是Json字符串或者是JavaBean对象
     */
    @AfterReturning(value = "execution(* *(..)) && @annotation(logEvent)", returning = "result")
    public void logEvent(LogEvent logEvent, Object result) {
        Transaction transaction = getTransaction();
        Event event = null;
        try {
            event = analyzeResult(result, logEvent);
        } catch (Throwable throwable) {
            Cat.logError("处理LogEvent注解时发生错误", throwable);
        } finally {
            if (transaction != null) {
                transaction.addChild(event);
            } else if (event != null) {
                event.complete();
            }
        }
    }

    private Event analyzeResult(Object result, LogEvent logEvent) {
        Event event;
        if (result instanceof String) {
            event = dealResultForJson(result, logEvent);
        } else {
            event = dealResultForBean(result, logEvent);
        }
        return event;
    }

    private Event dealResultForBean(Object result, LogEvent logEvent) {
        Event event;
        Object messageObj = getValueFromParam(result, logEvent.messageProperty());
        Object currentCodeObj = getValueFromParam(result, logEvent.codeProperty());
        if (messageObj != null && currentCodeObj != null) {
            event = Cat.newEvent(logEvent.type(), messageObj.toString());
            setStatusByCode(event, logEvent.expectedCodes(), currentCodeObj.toString());
            return event;
        } else {
            throw new ParseAnnotationException("Error reading the bean value : is Null! message:" + messageObj + " code:" + currentCodeObj);
        }
    }

    private Event dealResultForJson(Object result, LogEvent logEvent) {
        Event event;
        JSONObject jsonObject = JSON.parseObject(result.toString());
        Object messageObj = JsonUtil.getValue(jsonObject, logEvent.messageProperty());
        Object currentCodeObj = JsonUtil.getValue(jsonObject, logEvent.codeProperty());
        if (messageObj != null && currentCodeObj != null) {
            event = Cat.newEvent(logEvent.type(), messageObj.toString());
            setStatusByCode(event, logEvent.expectedCodes(), currentCodeObj.toString());
            return event;
        } else {
            throw new ParseAnnotationException("Error reading the json value : is Null! message:" + messageObj + " code:" + currentCodeObj);
        }
    }

    private void setStatusByCode(Event event, String[] codes, String code) {
        List<String> expectedCodes = Arrays.asList(codes);
        if (expectedCodes.contains(code)) {
            event.setStatus(Message.SUCCESS);
        } else {
            event.setStatus("Unexpected Code:" + code);
        }
    }

    private Transaction getTransaction() {
        Stack<Transaction> stack = transactionThreadLocal.get();
        if (stack != null && !stack.isEmpty()) {
            return stack.peek();
        } else {
            return null;
        }
    }

    @AfterThrowing(value = "execution(* *(..)) && @annotation(com.edu.JavaLearning.aop.aop.annotation.LogTransaction)", throwing = "exception")
    public void haveException(Throwable exception) {
        Cat.logError("调用业务方法出现异常", exception);
        Transaction transaction = getTransaction();
        if (transaction != null) {
            transaction.setStatus(exception);
        }
    }

    @After("execution(* *(..)) && @annotation(com.edu.JavaLearning.aop.aop.annotation.LogTransaction)")
    public void completeTransaction() {
        Transaction transaction = getTransaction();
        if (transaction != null) {
            transactionThreadLocal.get().pop().complete();
        }
        if (transactionThreadLocal.get().isEmpty()) {
            transactionThreadLocal.remove();
        }
    }


    private Map<String, Object> getValueFromParams(Object[] args, String[] name) {
        Map<String, Object> values = new HashMap<>(10);
        for (String aName : name) {
            for (Object perArg : args) {
                Object value = getValueFromParam(perArg, aName);
                if (value != null) {
                    values.put(aName, value);
                }
            }
        }

        return values;
    }

    private Object getValueFromParam(Object perArg, String fieldName) {
        Field field = cacheField.get(fieldName);
        if (field != null) {
            try {
                return field.get(perArg);
            } catch (IllegalAccessException e) {
                Cat.logError(e);
            }
        }

        Object result = null;
        Class clazz = perArg.getClass();
        do {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                //什么也不做，继续往继承的父类中寻找属性值
                clazz = clazz.getSuperclass();
                continue;
            }
            break;
        } while (clazz.getSuperclass() != Object.class);


        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(perArg);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            cacheField.put(fieldName, field);
        }

        return result;
    }

}
