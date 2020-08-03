package com.melson.webserver.dto;

import com.melson.base.entity.Store;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/3
 */
public class StoreDto {
    private String storeName;
    private String location;
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String areaCode;
    private String areaName;
    private String phone;
    private String communicateName;
    private String description;
    private String password;


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCommunicateName() {
        return communicateName;
    }

    public void setCommunicateName(String communicateName) {
        this.communicateName = communicateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Store GenerateStore(){
        Store store=new Store();
        store.setStoreName(this.storeName);
        store.setLocation(this.location);
        store.setProvinceCode(this.provinceCode);
        store.setProvinceName(this.provinceName);
        store.setCityCode(this.cityCode);
        store.setCityName(this.cityName);
        store.setAreaCode(this.areaCode);
        store.setAreaName(this.areaName);
        store.setPhone(this.phone);
        store.setCommunicateName(this.communicateName);
        store.setDescription(this.description);
        return store;
    }
}
