package com.melson.webserver.dto;

import javax.persistence.Transient;

/**
 * Created by Messi on 2020/9/11
 * @Description 商品目录操作载体
 */
public class ProductDto {
    private Integer id;
    private String storeCode;
    private String name;
    private String type;
    private String specification;
    private String unit;
    private String feature;
    private String categoryId;
    private String categoryName;
    private String categoryComment;
    private Integer categoryKeyId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryComment() {
        return categoryComment;
    }

    public void setCategoryComment(String categoryComment) {
        this.categoryComment = categoryComment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Integer getCategoryKeyId() {
        return categoryKeyId;
    }

    public void setCategoryKeyId(Integer categoryKeyId) {
        this.categoryKeyId = categoryKeyId;
    }
}
