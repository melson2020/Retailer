package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.IStoreDao;
import com.melson.base.entity.Store;
import com.melson.base.service.IStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class StoreImpl extends AbstractService<Store> implements IStore {
    private final IStoreDao storeDao;

    public StoreImpl(IStoreDao storeDao) {
        this.storeDao = storeDao;
    }

    @Override
    public JpaRepository<Store, String> getRepository() {
        return storeDao;
    }
}
