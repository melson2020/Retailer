package com.melson.webserver.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/25
 */
@Entity
@Table(name = "storage_out_bill_detail")
public class StorageOutBillDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String outBillCode;
    private Integer productId;
    private String productName;
    private Integer supplyId;
    private String supplyName;
    private String batchNo;
    private BigDecimal unitPriceIn;
    private Integer vatIn;
    private String taxRateIn;
    private Integer discount;
    private Integer vatOut;
    private String taxRateOut;
    private Integer outCount;
    private BigDecimal unitPriceOut;
    private BigDecimal profit;
    private String countUnit;

    public String getCountUnit() {
        return countUnit;
    }

    public void setCountUnit(String countUnit) {
        this.countUnit = countUnit;
    }

    public BigDecimal getUnitPriceOut() {
        return unitPriceOut;
    }

    public void setUnitPriceOut(BigDecimal unitPriceOut) {
        this.unitPriceOut = unitPriceOut;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOutBillCode() {
        return outBillCode;
    }

    public void setOutBillCode(String outBillCode) {
        this.outBillCode = outBillCode;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getUnitPriceIn() {
        return unitPriceIn;
    }

    public void setUnitPriceIn(BigDecimal unitPriceIn) {
        this.unitPriceIn = unitPriceIn;
    }

    public Integer getVatIn() {
        return vatIn;
    }

    public void setVatIn(Integer vatIn) {
        this.vatIn = vatIn;
    }

    public String getTaxRateIn() {
        return taxRateIn;
    }

    public void setTaxRateIn(String taxRateIn) {
        this.taxRateIn = taxRateIn;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getVatOut() {
        return vatOut;
    }

    public void setVatOut(Integer vatOut) {
        this.vatOut = vatOut;
    }

    public String getTaxRateOut() {
        return taxRateOut;
    }

    public void setTaxRateOut(String taxRateOut) {
        this.taxRateOut = taxRateOut;
    }

    public Integer getOutCount() {
        return outCount;
    }

    public void setOutCount(Integer outCount) {
        this.outCount = outCount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
