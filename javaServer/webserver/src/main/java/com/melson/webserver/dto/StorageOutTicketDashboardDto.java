package com.melson.webserver.dto;

import java.math.BigDecimal;

/**
 * @Author Nelson
 * @Description dashboard 页面使用的数据载体
 * @Date 2020/12/23
 */
public class StorageOutTicketDashboardDto {
    private Integer productId;
    private String productName;
    private String employeeId;
    private String employeeName;
    private BigDecimal totalPrice;

    public StorageOutTicketDashboardDto(){}

    public StorageOutTicketDashboardDto(Integer productId, String productName, String employeeId, String employeeName, BigDecimal totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalPrice = totalPrice;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
