package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.IStoreEmployeeDao;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.service.IStoreEmployee;
import com.melson.base.utils.MD5Util;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class StroeEmployeeImpl extends AbstractService<StoreEmployee> implements IStoreEmployee {
    private final IStoreEmployeeDao storeEmployeeDao;

    public StroeEmployeeImpl(IStoreEmployeeDao storeEmployeeDao) {
        this.storeEmployeeDao = storeEmployeeDao;
    }

    @Override
    public JpaRepository<StoreEmployee, String> getRepository() {
        return storeEmployeeDao;
    }

    @Override
    public List<StoreEmployee> findAll() {
        return storeEmployeeDao.findAll();
    }

    @Override
    public StoreEmployee Login(StoreEmployee employee) {
        String md5Pass= MD5Util.string2MD5(employee.getPassword());
        StoreEmployee existEmployee=storeEmployeeDao.findByLoginNameAndPassword(employee.getLoginName(),md5Pass);
        return existEmployee;
    }

    @Override
    public List<StoreEmployee> findByStoreCode(String stroeCode) {
        return storeEmployeeDao.findAllByStoreCode(stroeCode);
    }
}
