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
    private String productName;
    private Integer count;
    private String countUnit;
    //batchType 为IN时 入库单号  C时 盘点单号
    private String storageInCode;
    private Double discount;
    private String taxRate;
    private BigDecimal totalPrice;
    //批次类型  IN 入库生成  C 盘点生成
    private String batchType;
    private BigDecimal netIn;   //净价格/unit （扣除返点）
    private BigDecimal tepIn;   //去税价格/unit
    private BigDecimal taxIn;   //税金/unit


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
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
}
