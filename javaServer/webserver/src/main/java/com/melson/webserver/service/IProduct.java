package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.dto.ProductDto;
import com.melson.webserver.dto.ProductImportDto;
import com.melson.webserver.entity.Product;
import com.melson.webserver.entity.Supply;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/25
 */
public interface IProduct extends IService<Product> {
    boolean SaveImportedList(ProductImportDto dto);
//    List<Product> FindUsingList(String storeCode);

    List<ProductDto> FindProductList(String storeCode);

    Integer DeleteProduct(ProductDto productDto);


    ProductDto Query(ProductDto product);

    ProductDto SaveProduct(ProductDto productDto);
}
