package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.dto.ProductImportDto;
import com.melson.webserver.entity.Product;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/25
 */
public interface IProduct extends IService<Product> {
    boolean SaveImportedList(ProductImportDto dto);
}
