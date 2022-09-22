package com.hzwotu.operationlogsdk.po;

/**
 * @Author chenl
 * @Date 2022/9/16 3:08 下午
 */

public class UserInfo {
    private String userCode;
    private String userName;
    private String mobile;

    public UserInfo() {
    }

    public UserInfo(String userCode, String userName, String mobile) {
        this.userCode = userCode;
        this.userName = userName;
        this.mobile = mobile;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
