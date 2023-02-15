package com.chen.operationlogstarter.aspect.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @Author chenl
 * @Date 2022/9/14 2:12 下午
 */
@Configuration
@ConfigurationProperties(value = "com.wotu.operationlog")
public class OperationLogConfigProperties {
    private String enName;
    private String zhName;


    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) throws Exception {
        if (StringUtils.isEmpty(enName)){
            throw new Exception("com.wotu.operationlog.enName配置未配置");
        }
        this.enName = enName;
    }

    public String getZhName() {
        return zhName;
    }


    public void setZhName(String zhName) throws Exception{
        if (StringUtils.isEmpty(enName)){
            throw new Exception("com.wotu.operationlog.zhName配置未配置");
        }
        this.zhName = zhName;
    }

}
