package com.melson.webserver.entity;

import javax.persistence.*;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@Entity
@Table(name = "product_storage")
public class ProductStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String storeCode;
    private Integer productId;
    private String productName;
    private String productType;
    private String productSpecification;
    private Integer count;
    private String unit;
    private String searchType;
    @Transient
    private List<ProductBatch> batchList;

    public List<ProductBatch> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<ProductBatch> batchList) {
        this.batchList = batchList;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductSpecification() {
        return productSpecification;
    }

    public void setProductSpecification(String productSpecification) {
        this.productSpecification = productSpecification;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
