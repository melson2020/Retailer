package com.melson.webserver.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/14
 */
@Entity
@Table(name = "storage_count_ticket")
public class StorageCountTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private String type;
    private String employeeId;
    private String employeeName;
    private String storeCode;
    private String date;
    private Date createTime;
    //盘点单状态标识  1 已创建 2已导出 3已导入 4 完成
    private Integer status;
    private String description;
    private String productType;
    private String excelExportPath;
    private String excelImportPath;
    private String excelExportFileName;
    private String excelImportFileName;

    public String getExcelExportFileName() {
        return excelExportFileName;
    }

    public void setExcelExportFileName(String excelExportFileName) {
        this.excelExportFileName = excelExportFileName;
    }

    public String getExcelImportFileName() {
        return excelImportFileName;
    }

    public void setExcelImportFileName(String excelImportFileName) {
        this.excelImportFileName = excelImportFileName;
    }

    public String getExcelExportPath() {
        return excelExportPath;
    }

    public void setExcelExportPath(String excelExportPath) {
        this.excelExportPath = excelExportPath;
    }

    public String getExcelImportPath() {
        return excelImportPath;
    }

    public void setExcelImportPath(String excelImportPath) {
        this.excelImportPath = excelImportPath;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
