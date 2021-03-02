package com.melson.webserver.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/18
 */
@Entity
@Table(name = "storage_out_ticket")
public class StorageOutTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date createTime;
    private String date;
    private String code;
    private String storeCode;
    private String employeeId;
    private String employeeName;
    private String type;
    private String description;
    private Integer categroyCount;
    private String billCode;
    private String customerName;
    private Integer customerId;
    //出库单状态值  0 初始值 1 有退货 2已退完
    private Integer status;
    private String deliveryCode;
    private String invoiceCode;

    @Transient
    private List<StorageOutDetail> details;
    @Transient
    private List<GoodsReturnRecord> returnList;

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public List<StorageOutDetail> getDetails() {
        return details;
    }

    public void setDetails(List<StorageOutDetail> details) {
        this.details = details;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategroyCount() {
        return categroyCount;
    }

    public void setCategroyCount(Integer categroyCount) {
        this.categroyCount = categroyCount;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<GoodsReturnRecord> getReturnList() {
        return returnList;
    }

    public void setReturnList(List<GoodsReturnRecord> returnList) {
        this.returnList = returnList;
    }
}
