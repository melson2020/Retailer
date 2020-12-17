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
    private BigDecimal netIn;   //净价格/unit （扣除返点）
    private BigDecimal tepIn;   //去税进价/unit
    private BigDecimal taxIn;   //税金/unit
    private BigDecimal tepOut;  //去税售价/unit
    private BigDecimal taxOut;  //税金/unit
    private String customerName;
    private Integer customerId;

    private String outDetailCode;

    private BigDecimal unitProfit;



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

    public BigDecimal getNetIn() {
        return netIn;
    }

    public void setNetIn(BigDecimal netIn) {
        this.netIn = netIn;
    }

    public BigDecimal getTepIn() {
        return tepIn;
    }

    public void setTepIn(BigDecimal tepIn) {
        this.tepIn = tepIn;
    }

    public BigDecimal getTaxIn() {
        return taxIn;
    }

    public void setTaxIn(BigDecimal taxIn) {
        this.taxIn = taxIn;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }


    public String getOutDetailCode() {
        return outDetailCode;
    }

    public void setOutDetailCode(String outDetailCode) {
        this.outDetailCode = outDetailCode;
    }
    public BigDecimal getUnitProfit() {
        return unitProfit;
    }

    public void setUnitProfit(BigDecimal unitProfit){
            this.unitProfit = unitProfit;
        }
}
