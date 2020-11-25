package com.melson.webserver.dto;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/26
 */
//盘点库存数据结构
public class ProductStorageDto {
    private Integer productId;
    private String productName;
    private String type;
    private String specification;
    private String batchNo;
    private Integer supplyId;
    private String supplyName;
    //现有数量
    private Integer count;
    private String countUnit;
    //盘点数量
    private Integer counted;
    //现有总数
    private Integer totalCount;
    //盘点总数
    private Integer totalCounted;
    //合并数量
    private Integer spanCount;

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    public Integer getTotalCounted() {
        return totalCounted;
    }

    public void setTotalCounted(Integer totalCounted) {
        this.totalCounted = totalCounted;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
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

    public Integer getCounted() {
        return counted;
    }

    public void setCounted(Integer counted) {
        this.counted = counted;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(Integer spanCount) {
        this.spanCount = spanCount;
    }
}
