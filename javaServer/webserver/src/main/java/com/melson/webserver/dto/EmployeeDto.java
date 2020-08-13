package com.melson.webserver.dto;

import org.springframework.util.StringUtils;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/12
 */
public class EmployeeDto {
    private String userId;
    private String loginName;
    private String oldPass;
    private String newPass;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public boolean hasEmptyParams(){
        return StringUtils.isEmpty(this.userId)||StringUtils.isEmpty(this.loginName)||StringUtils.isEmpty(this.oldPass)||StringUtils.isEmpty(this.newPass);
    }
}
