package com.melson.webserver.dto;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/16
 */
public class StorageOutTicketInfoDto {
    private Integer id;
    private String date;
    private String code;
    private String customerName;
    private String productName;
    private String batchNo;
    private String supplyName;
    private Integer outCount;
    private String countUnit;
    private String storeCode;

    public StorageOutTicketInfoDto() {

    }

    public StorageOutTicketInfoDto(Integer id, String date, String code, String customerName, String productName, String batchNo, String supplyName, Integer outCount, String countUnit, String storeCode) {
        this.id = id;
        this.date = date;
        this.code = code;
        this.customerName = customerName;
        this.productName = productName;
        this.batchNo = batchNo;
        this.supplyName = supplyName;
        this.outCount = outCount;
        this.countUnit = countUnit;
        this.storeCode = storeCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public Integer getOutCount() {
        return outCount;
    }

    public void setOutCount(Integer outCount) {
        this.outCount = outCount;
    }

    public String getCountUnit() {
        return countUnit;
    }

    public void setCountUnit(String countUnit) {
        this.countUnit = countUnit;
    }
}
