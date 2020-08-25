package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IProductDao;
import com.melson.webserver.entity.Product;
import com.melson.webserver.service.IProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/25
 */
@Service
public class ProductImpl extends AbstractService<Product> implements IProduct {
    private final IProductDao productDao;

    public ProductImpl(IProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public JpaRepository<Product, String> getRepository() {
        return productDao;
    }
}
