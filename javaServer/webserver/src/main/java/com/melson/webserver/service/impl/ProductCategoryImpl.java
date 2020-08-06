package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IProductCategoryDao;
import com.melson.webserver.entity.ProductCategory;
import com.melson.webserver.service.IProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
@Service
public class ProductCategoryImpl extends AbstractService<ProductCategory> implements IProductCategory {
    private final IProductCategoryDao productCategoryDao;

    public ProductCategoryImpl(IProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }

    @Override
    public JpaRepository<ProductCategory, String> getRepository() {
        return productCategoryDao;
    }

    @Override
    public List<ProductCategory> findAll() {
        return productCategoryDao.findAll();
    }
}
