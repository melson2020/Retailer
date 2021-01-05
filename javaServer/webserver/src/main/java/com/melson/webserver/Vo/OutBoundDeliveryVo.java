package com.melson.webserver.Vo;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2021/1/4
 */
public class OutBoundDeliveryVo {
    private String startDate;
    private String endDate;
    private String customerId;
    private String productId;
    private String employeeId;
    private List<OutBoundVo> outBoundVoList;

    public OutBoundDeliveryVo(String startDate, String endDate, String customerId, String productId, String employeeId, List<OutBoundVo> outBoundVoList) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerId = customerId;
        this.productId = productId;
        this.employeeId = employeeId;
        this.outBoundVoList = outBoundVoList;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<OutBoundVo> getOutBoundVoList() {
        return outBoundVoList;
    }

    public void setOutBoundVoList(List<OutBoundVo> outBoundVoList) {
        this.outBoundVoList = outBoundVoList;
    }
}
