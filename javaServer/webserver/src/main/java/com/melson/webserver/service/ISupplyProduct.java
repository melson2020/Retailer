package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.entity.SupplyProduct;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
public interface ISupplyProduct extends IService<SupplyProduct> {
    List<SupplyProduct> findAll();

}
