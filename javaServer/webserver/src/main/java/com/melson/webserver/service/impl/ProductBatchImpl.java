package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IProductBatchDao;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.service.IProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@Service
public class ProductBatchImpl extends AbstractService<ProductBatch> implements IProductBatch {
    private final IProductBatchDao productBatchDao;

    public ProductBatchImpl(IProductBatchDao productBatchDao) {
        this.productBatchDao = productBatchDao;
    }

    @Override
    public JpaRepository<ProductBatch, String> getRepository() {
        return productBatchDao;
    }
}
