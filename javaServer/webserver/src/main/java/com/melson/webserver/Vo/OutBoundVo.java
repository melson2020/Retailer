package com.melson.webserver.Vo;

import java.math.BigDecimal;

/**
 * Created by Messi on 2020/11/13
 */
public class OutBoundVo {
    private String date;
    private String outBoundNo;
    private String salesName;
    private String product;
    private String supply;
    private String batchNo;
    private BigDecimal priceIn;
    private BigDecimal priceOut;
    private Integer outCount;
    private BigDecimal profit;

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
}
