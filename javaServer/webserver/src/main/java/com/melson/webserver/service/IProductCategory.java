package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.entity.ProductCategory;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
public interface IProductCategory extends IService<ProductCategory> {
    List<ProductCategory> findAll();
}
