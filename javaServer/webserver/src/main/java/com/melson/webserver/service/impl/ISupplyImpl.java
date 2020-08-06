package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.ISupplyDao;
import com.melson.webserver.entity.Supply;
import com.melson.webserver.service.ISupply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
@Service
public class ISupplyImpl extends AbstractService <Supply> implements ISupply {
    private final ISupplyDao supplyDao;

    public ISupplyImpl(ISupplyDao supplyDao) {
        this.supplyDao = supplyDao;
    }

    @Override
    public JpaRepository<Supply, String> getRepository() {
        return supplyDao;
    }

    @Override
    public List<Supply> findAll() {
        return supplyDao.findAll();
    }
}
