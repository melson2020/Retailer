package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.ISupplyRelationDao;
import com.melson.webserver.entity.SupplyRelation;
import com.melson.webserver.service.ISupplyRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
@Service
public class ISupplyRelationImpl extends AbstractService<SupplyRelation> implements ISupplyRelation {
    private final ISupplyRelationDao supplyRelationDao;

    public ISupplyRelationImpl(ISupplyRelationDao supplyRelationDao) {
        this.supplyRelationDao = supplyRelationDao;
    }

    @Override
    public JpaRepository<SupplyRelation, String> getRepository() {
        return supplyRelationDao;
    }

    @Override
    public List<SupplyRelation> findAll() {
        return supplyRelationDao.findAll();
    }
}
