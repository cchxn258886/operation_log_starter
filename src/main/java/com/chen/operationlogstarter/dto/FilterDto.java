package com.chen.operationlogstarter.dto;


/**
 * @Author chenl
 * @Date 2022/9/8 8:38 下午
 */

public class FilterDto {
    private String modelName = "";
    private String modelResult = "";

    public FilterDto(String modelName, String modelResult) {
        this.modelName = modelName;
        this.modelResult = modelResult;
    }

    public FilterDto() {
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelResult() {
        return modelResult;
    }

    public void setModelResult(String modelResult) {
        this.modelResult = modelResult;
    }
}
