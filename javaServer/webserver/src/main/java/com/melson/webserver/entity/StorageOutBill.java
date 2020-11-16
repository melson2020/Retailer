package com.melson.webserver.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/25
 */
@Entity
@Table(name = "storage_out_bill")
public class StorageOutBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String employeeId;
    private String employeeName;
    private BigDecimal cost;
    //入库总价
    private BigDecimal totalPriceIn;
    //出库总价
    private BigDecimal totalPriceOut;
    //去税价格
    private BigDecimal sales;
    //总利润
    private BigDecimal profit;
    private Date createTime;
    private String date;
    private String code;
    private String storeCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getTotalPriceIn() {
        return totalPriceIn;
    }

    public void setTotalPriceIn(BigDecimal totalPriceIn) {
        this.totalPriceIn = totalPriceIn;
    }

    public BigDecimal getTotalPriceOut() {
        return totalPriceOut;
    }

    public void setTotalPriceOut(BigDecimal totalPriceOut) {
        this.totalPriceOut = totalPriceOut;
    }

    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
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
}
