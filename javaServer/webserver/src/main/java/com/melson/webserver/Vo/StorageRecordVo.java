package com.melson.webserver.Vo;

import java.util.Date;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/11/24
 */
public class StorageRecordVo {
    private String date;
    private String code;
    private String batchNo;
    private Integer productId;
    private String productName;
    private Integer count;
    private Integer beforeCount;
    private Integer afterCount;
    private String type;
    private String action;
    private Date createTime;
    private String supplyName;

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public StorageRecordVo(){

    }

    public StorageRecordVo(String date, String code, String batchNo, Integer productId, String productName, Integer count, Integer beforeCount, Integer afterCount, String type, String action, Date createTime, String supplyName) {
        this.date = date;
        this.code = code;
        this.batchNo = batchNo;
        this.productId = productId;
        this.productName = productName;
        this.count = count;
        this.beforeCount = beforeCount;
        this.afterCount = afterCount;
        this.type = type;
        this.action = action;
        this.createTime = createTime;
        this.supplyName = supplyName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getBeforeCount() {
        return beforeCount;
    }

    public void setBeforeCount(Integer beforeCount) {
        this.beforeCount = beforeCount;
    }

    public Integer getAfterCount() {
        return afterCount;
    }

    public void setAfterCount(Integer afterCount) {
        this.afterCount = afterCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
