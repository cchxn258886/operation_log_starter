package com.chen.operationlogstarter.po;

/**
 * @Author chenl
 * @Date 2022/9/15 6:00 下午
 */

public class OperationLogTypeEntity {
    private Integer id;
    private String enName;
    private String zhName;

    public OperationLogTypeEntity() {
    }

    public OperationLogTypeEntity(Integer id, String enName, String zhName) {
        this.id = id;
        this.enName = enName;
        this.zhName = zhName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}
