package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.IStoreEmployeeDao;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.service.IStroeEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class StroeEmployeeImpl extends AbstractService<StoreEmployee> implements IStroeEmployee {
    private final IStoreEmployeeDao storeEmployeeDao;

    public StroeEmployeeImpl(IStoreEmployeeDao storeEmployeeDao) {
        this.storeEmployeeDao = storeEmployeeDao;
    }

    @Override
    public JpaRepository<StoreEmployee, String> getRepository() {
        return storeEmployeeDao;
    }
}
