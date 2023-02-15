package com.chen.operationlogstarter.aspect.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author chenl
 * @Date 2022/9/15 11:55 上午
 */
@Configuration
@ComponentScan(basePackages = {"com.hzwotu.*"})
public class OperationLogAutoConfig {

}
