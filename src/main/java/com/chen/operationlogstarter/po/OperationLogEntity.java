package com.chen.operationlogstarter.po;

import com.alibaba.fastjson2.annotation.JSONField;
import com.hzwotu.operationlogsdk.dto.FilterDto;
import com.hzwotu.operationlogsdk.utils.IDUtil;

import java.time.LocalDateTime;

/**
 * @Author chenl
 * @Date 2022/9/8 3:10 下午
 */
public class OperationLogEntity {

    private String code;
    private String module;
    private String action;
    private String keyCode;
    private String param;
    private String adminCode;
    private String ip;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    private String adminName;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OperationLogEntity{" +
                "code='" + code + '\'' +
                ", module='" + module + '\'' +
                ", action='" + action + '\'' +
                ", keyCode='" + keyCode + '\'' +
                ", param='" + param + '\'' +
                ", adminCode='" + adminCode + '\'' +
                ", ip='" + ip + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }


    public void create(OperationLogEntity operationLogEntity, String adminCode, FilterDto filterDto){
        operationLogEntity.setCreatedAt(LocalDateTime.now());
        operationLogEntity.setCode(IDUtil.getIdGen());
        operationLogEntity.setAdminCode(adminCode);
        operationLogEntity.setModule(filterDto.getModelName());
        operationLogEntity.setAction(filterDto.getModelResult());
    }
}
