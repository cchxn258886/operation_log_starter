package com.hzwotu.operationlogsdk.aspect.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author chenl
 * @Date 2022/9/14 2:12 下午
 */
@ConfigurationProperties(value = "com.wotu.operationlog")
public class OperationLogConfigProperties {
    private Boolean enable;
    private String jdbcUrl;
    private String userName;
    private String passWord;
    private String idServiceAddress;
    private String adminServiceAddress;

    private String enName;
    private String zhName;


    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }


    public String getAdminServiceAddress() {
        return adminServiceAddress;
    }

    public void setAdminServiceAddress(String adminServiceAddress) {
        this.adminServiceAddress = adminServiceAddress;
    }

    public String getIdServiceAddress() {
        return idServiceAddress;
    }

    public void setIdServiceAddress(String idServiceAddress) {
        this.idServiceAddress = idServiceAddress;
    }

    public Boolean getEnable() {
        return enable;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
