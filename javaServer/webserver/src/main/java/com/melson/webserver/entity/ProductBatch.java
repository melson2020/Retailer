package com.melson.webserver.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@Entity
@Table(name = "product_batch")
public class ProductBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String storeCode;
    private String batchNo;
    private Integer supplyId;
    private String supplyName;
    private BigDecimal price;
    private String priceUnit;
    private Integer vat;
    //是否库存为0 1 是 0 否
    private Integer finished;
    private Integer productId;
    private Integer count;
    private String countUnit;
    private String storageInCode;
    private Integer discount;
    private String taxRate;
    private BigDecimal totalPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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


    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Integer getVat() {
        return vat;
    }

    public void setVat(Integer vat) {
        this.vat = vat;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public String getStorageInCode() {
        return storageInCode;
    }

    public void setStorageInCode(String storageInCode) {
        this.storageInCode = storageInCode;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
