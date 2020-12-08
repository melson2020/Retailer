package com.melson.webserver.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/25
 */
@Entity
@Table(name = "storage_out_detail")
public class StorageOutDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer productId;
    private String productName;
    private String storageInBatchNo;
    private Integer vat;
    private String taxRate;
    private Integer outCount;
    private String countUnit;
    private BigDecimal outPrice;
    private String storeCode;
    private String outTicketCode;
    private BigDecimal totalPrice;
    private Integer supplyId;
    private String supplyName;
    private Integer beforeOutCount;
    private Integer afterOutCount;
    private BigDecimal tepOut;   //去税售价/unit
    private BigDecimal taxOut;   //税金/unit

    public Integer getBeforeOutCount() {
        return beforeOutCount;
    }

    public void setBeforeOutCount(Integer beforeOutCount) {
        this.beforeOutCount = beforeOutCount;
    }

    public Integer getAfterOutCount() {
        return afterOutCount;
    }

    public void setAfterOutCount(Integer afterOutCount) {
        this.afterOutCount = afterOutCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStorageInBatchNo() {
        return storageInBatchNo;
    }

    public void setStorageInBatchNo(String storageInBatchNo) {
        this.storageInBatchNo = storageInBatchNo;
    }

    public Integer getVat() {
        return vat;
    }

    public void setVat(Integer vat) {
        this.vat = vat;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getOutCount() {
        return outCount;
    }

    public void setOutCount(Integer outCount) {
        this.outCount = outCount;
    }

    public BigDecimal getOutPrice() {
        return outPrice;
    }

    public void setOutPrice(BigDecimal outPrice) {
        this.outPrice = outPrice;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getOutTicketCode() {
        return outTicketCode;
    }

    public void setOutTicketCode(String outTicketCode) {
        this.outTicketCode = outTicketCode;
    }

    public String getCountUnit() {
        return countUnit;
    }

    public void setCountUnit(String countUnit) {
        this.countUnit = countUnit;
    }

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public BigDecimal getTepOut() {
        return tepOut;
    }

    public void setTepOut(BigDecimal tepOut) {
        this.tepOut = tepOut;
    }

    public BigDecimal getTaxOut() {
        return taxOut;
    }

    public void setTaxOut(BigDecimal taxOut) {
        this.taxOut = taxOut;
    }
}

