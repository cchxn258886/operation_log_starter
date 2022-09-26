package com.hzwotu.operationlogsdk.aspect.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author chenl
 * @Date 2022/9/15 11:55 上午
 */
@Configuration
@ConditionalOnClass(OperationLogConfigProperties.class)
@EnableConfigurationProperties(OperationLogConfigProperties.class)
@ComponentScan(basePackages = {"com.hzwotu.*"})
public class OperationLogAutoConfig {

}
