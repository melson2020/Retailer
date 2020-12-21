package com.melson.webserver.Vo;

import java.math.BigDecimal;

/**
 * Created by Messi on 2020/11/13
 */
public class OutBoundVo {
    private String date;
    private String outBoundNo;
    private String customerName;
    private String salesName;
    private String product;
    private String supply;
    private String batchNo;
    private BigDecimal priceIn;
    private BigDecimal priceOut;
    private Integer outCount;
    private BigDecimal totalPrice;
    private BigDecimal unitProfit;
    private BigDecimal profit;
    private Integer returnCount;

    public OutBoundVo() {
    }

    public OutBoundVo(String date, String outBoundNo, String customerName, String salesName, String product, String supply, String batchNo, BigDecimal priceIn, BigDecimal priceOut, Integer outCount, BigDecimal totalPrice, BigDecimal unitProfit, BigDecimal profit, Integer returnCount) {
        this.date = date;
        this.outBoundNo = outBoundNo;
        this.customerName = customerName;
        this.salesName = salesName;
        this.product = product;
        this.supply = supply;
        this.batchNo = batchNo;
        this.priceIn = priceIn;
        this.priceOut = priceOut;
        this.outCount = outCount;
        this.totalPrice = totalPrice;
        this.unitProfit = unitProfit;
        this.profit = profit;
        this.returnCount = returnCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOutBoundNo() {
        return outBoundNo;
    }

    public void setOutBoundNo(String outBoundNo) {
        this.outBoundNo = outBoundNo;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getPriceIn() {
        return priceIn;
    }

    public void setPriceIn(BigDecimal priceIn) {
        this.priceIn = priceIn;
    }

    public BigDecimal getPriceOut() {
        return priceOut;
    }

    public void setPriceOut(BigDecimal priceOut) {
        this.priceOut = priceOut;
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

    public BigDecimal getUnitProfit() {
        return unitProfit;
    }

    public void setUnitProfit(BigDecimal unitProfit) {
        this.unitProfit = unitProfit;
    }

    public Integer getReturnCount() {
        return returnCount==null?0:returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
