package com.melson.webserver.dto;

import com.melson.webserver.entity.Product;
import com.melson.webserver.entity.ProductCategory;

import java.util.List;

/**
 * @Author Nelson
 * @Description 用户商品目录导入载体
 * @Date 2020/8/25
 */
public class ProductImportDto {
    private String storeCode;
    private List<Product> productList;
    private List<ProductCategory> categoryList;

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<ProductCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<ProductCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
