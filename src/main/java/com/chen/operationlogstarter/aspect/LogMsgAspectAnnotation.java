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

    String expression() default "";
    /**
     * 如果mapperName 为空 取tableName 拼上mapper
     * */
    String mapperName() default "";
//    String tableName() ;

    /**
     * 后续获取修改前数据 初步想法实现
    * targetName 传入目标的原始类 反射出来的数据应该是Object
    * */
//    String targetName();

}
