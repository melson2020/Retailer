package com.melson.webserver.Vo;

import java.math.BigDecimal;

/**
 * Created by Messi on 2020/12/23
 */
public class WechatOutBoundVo {
    private String date;
    private String customerName;
    private String product;
    private String supply;
    private BigDecimal priceOut;
    private Integer count;
    private BigDecimal totalPrice;
    private String countUnit;

    public WechatOutBoundVo() {

    }

    public WechatOutBoundVo(String date, String customerName, String product, String supply, BigDecimal priceOut, Integer count, BigDecimal totalPrice, String countUnit) {
        this.date = date;
        this.customerName = customerName;
        this.product = product;
        this.supply = supply;
        this.priceOut = priceOut;
        this.count = count;
        this.totalPrice = totalPrice;
        this.countUnit = countUnit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public BigDecimal getPriceOut() {
        return priceOut;
    }

    public void setPriceOut(BigDecimal priceOut) {
        this.priceOut = priceOut;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCountUnit() {
        return countUnit;
    }

    public void setCountUnit(String countUnit) {
        this.countUnit = countUnit;
    }
}
