package com.melson.webserver.Vo;

/**
 * @Author Nelson
 * @Description
 * @Date 2021/1/7
 */
public class ShareStorageVo {
    private String productName;
    private String productType;
    private String specification;
    private String supplyName;
    private Integer subCount;
    private String unit;

    public ShareStorageVo(){

    }

    public ShareStorageVo(String productName, String productType, String specification, String supplyName, Integer subCount, String unit) {
        this.productName = productName;
        this.productType = productType;
        this.specification = specification;
        this.supplyName = supplyName;
        this.subCount = subCount;
        this.unit = unit;
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

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public Integer getSubCount() {
        return subCount;
    }

    public void setSubCount(Integer subCount) {
        this.subCount = subCount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
