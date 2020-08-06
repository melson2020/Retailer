package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.ISupplyProductDao;
import com.melson.webserver.entity.SupplyProduct;
import com.melson.webserver.service.ISupplyProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
@Service
public class ISupplyProductImpl extends AbstractService<SupplyProduct> implements ISupplyProduct {
    private final ISupplyProductDao supplyProductDao;

    public ISupplyProductImpl(ISupplyProductDao supplyProductDao) {
        this.supplyProductDao = supplyProductDao;
    }

    @Override
    public JpaRepository<SupplyProduct, String> getRepository() {
        return supplyProductDao;
    }

    @Override
    public List<SupplyProduct> findAll() {
        return supplyProductDao.findAll();
    }
}
