package com.hzwotu.operationlogsdk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "管理员基本信息请求列表")
public class AdminListRequestDto {
    @ApiModelProperty(value = "管理员编码" ,required = true)
    private List<String> codeList;

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }
}