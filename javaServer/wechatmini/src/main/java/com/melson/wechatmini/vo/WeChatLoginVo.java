package com.melson.wechatmini.vo;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/4
 */
public class WeChatLoginVo {
    private String code;
    private String rowData;
    private String signature;
    private String encryptedData;
    private String iv;
    private String loginName;
    private String password;
    private String storeCode;
    private String storeName;
    private String retailerUserId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRowData() {
        return rowData;
    }

    public void setRowData(String rowData) {
        this.rowData = rowData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRetailerUserId() {
        return retailerUserId;
    }

    public void setRetailerUserId(String retailerUserId) {
        this.retailerUserId = retailerUserId;
    }
}
