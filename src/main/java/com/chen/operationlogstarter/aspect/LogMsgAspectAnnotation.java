package com.chen.operationlogstarter.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author chenl
 * @Date 2022/8/16 5:49 下午
 * 自定义日志收集
 * 类名 别名 表达式
 * @LogMsgAspectAnnotation(expression="xxxx")
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMsgAspectAnnotation {
    /**
     *
     * 支持自定义日志
     * 默认是不需要参数 表示从httpReq中拿requestBody
     * 自定义日志输入记录删掉了 后续有需求在写
     * */
    String expression() default "";
}
