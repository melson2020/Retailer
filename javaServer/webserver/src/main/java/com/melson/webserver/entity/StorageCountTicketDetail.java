package com.melson.webserver.entity;

import javax.persistence.*;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/23
 */
@Entity
@Table(name = "storage_count_ticket_detail")
public class StorageCountTicketDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String storeCode;
    private Integer productId;
    private String productName;
    private String supplyName;
    private String type;
    private Integer count;
    private String ticketCode;
    private String batchNo;
    //1 总数变化 批次数量未变 0 批次数量变化
    private Integer totalCountChange;

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public Integer getTotalCountChange() {
        return totalCountChange;
    }

    public void setTotalCountChange(Integer totalCountChange) {
        this.totalCountChange = totalCountChange;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }
}
