package com.melson.webserver.Vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Messi on 2021/3/29
 */
public class StorageInReportVO {
    private String createTime;
    private String batchNo;
    private String employeeName;
    private String supplyName;
    private String productName;
    private Double discount;
    private Integer count;
    private String countUnit;
    private String taxRate;
    private BigDecimal totalPrice;


    public StorageInReportVO() {
    }

    public StorageInReportVO(String createTime, String batchNo, String employeeName, String supplyName, String productName, Double discount, Integer count, String countUnit, String taxRate, BigDecimal totalPrice) {
        this.createTime = createTime;
        this.batchNo = batchNo;
        this.employeeName = employeeName;
        this.supplyName = supplyName;
        this.productName = productName;
        this.discount = discount;
        this.count = count;
        this.countUnit = countUnit;
        this.taxRate = taxRate;
        this.totalPrice = totalPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCountUnit() {
        return countUnit;
    }

    public void setCountUnit(String countUnit) {
        this.countUnit = countUnit;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


}
