package com.melson.webserver.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/16
 */
@Entity
@Table(name = "goods_return_record")
public class GoodsReturnRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String storeCode;
    private String outTicketCode;
    private Integer productId;
    private String productName;
    private Integer supplyId;
    private String supplyName;
    private Integer customerId;
    private String customerName;
    private Integer count;
    private String countUnit;
    private BigDecimal totalPrice;
    //单价
    private BigDecimal priceUnit;
    //单利润
    private BigDecimal profitUnit;
    private BigDecimal totalProfit;
    private Integer batchId;
    private String batchNo;
    private Date createTime;
    private String date;
    private String operationEmployeeName;
    private String operationEmployeeUserId;
    private String billCode;
    private String outDetailCode;
    private Integer beforeCount;
    private Integer afterCount;
    private Date outTicketTime;

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

    public String getOutTicketCode() {
        return outTicketCode;
    }

    public void setOutTicketCode(String outTicketCode) {
        this.outTicketCode = outTicketCode;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(BigDecimal priceUnit) {
        this.priceUnit = priceUnit;
    }

    public BigDecimal getProfitUnit() {
        return profitUnit;
    }

    public void setProfitUnit(BigDecimal profitUnit) {
        this.profitUnit = profitUnit;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getOperationEmployeeName() {
        return operationEmployeeName;
    }

    public void setOperationEmployeeName(String operationEmployeeName) {
        this.operationEmployeeName = operationEmployeeName;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getOutDetailCode() {
        return outDetailCode;
    }

    public void setOutDetailCode(String outDetailCode) {
        this.outDetailCode = outDetailCode;
    }

    public Integer getBeforeCount() {
        return beforeCount;
    }

    public void setBeforeCount(Integer beforeCount) {
        this.beforeCount = beforeCount;
    }

    public Integer getAfterCount() {
        return afterCount;
    }

    public void setAfterCount(Integer afterCount) {
        this.afterCount = afterCount;
    }

    public String getOperationEmployeeUserId() {
        return operationEmployeeUserId;
    }

    public void setOperationEmployeeUserId(String operationEmployeeUserId) {
        this.operationEmployeeUserId = operationEmployeeUserId;
    }

    public Date getOutTicketTime() {
        return outTicketTime;
    }

    public void setOutTicketTime(Date outTicketTime) {
        this.outTicketTime = outTicketTime;
    }
}
