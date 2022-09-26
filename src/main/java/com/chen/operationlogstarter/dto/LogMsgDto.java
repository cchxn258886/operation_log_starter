package com.hzwotu.operationlogsdk.dto;

/**
 * @Author chenl
 * @Date 2022/8/16 8:40 下午
 */

public class LogMsgDto {
    private String classType;
    private Object value;

    public LogMsgDto() {
    }

    public LogMsgDto(String classType, Object value) {
        this.classType = classType;
        this.value = value;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
