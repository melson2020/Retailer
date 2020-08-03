package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.IStoreDao;
import com.melson.base.dao.IStoreEmployeeDao;
import com.melson.base.entity.Store;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.base.service.IStore;
import com.melson.base.utils.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class StoreImpl extends AbstractService<Store> implements IStore {
    private final IStoreDao storeDao;
    private final IStoreEmployeeDao storeEmployeeDao;

    public StoreImpl(IStoreDao storeDao,IStoreEmployeeDao storeEmployeeDao) {
        this.storeDao = storeDao;
        this.storeEmployeeDao=storeEmployeeDao;
    }

    @Override
    public JpaRepository<Store, String> getRepository() {
        return storeDao;
    }

    @Override
    @Transactional
    public boolean RegisterStore(Store store,String password) {
        String storeCode= UUID.randomUUID().toString();
        store.setCode(storeCode);
        Store saved=storeDao.save(store);
        if(saved!=null){
           StoreEmployee employee=GenerateAdminUserForStore(saved,password);
           if(storeEmployeeDao.save(employee)!=null){
               return true;
           }else {
               return false;
           }
        }else {
            return false;
        }
    }

    @Override
    public boolean CheckPhone(String phone) {
         String phoneNum=storeDao.findByPhone(phone);
         return StringUtils.isEmpty(phoneNum);
    }

    private StoreEmployee GenerateAdminUserForStore(Store store,String password){
        StoreEmployee employee=new StoreEmployee();
        employee.setStoreCode(store.getCode());
        employee.setGender(0);
        employee.setUserName(store.getStoreName()+"管理人员");
        employee.setUserId(UUID.randomUUID().toString());
        employee.setPhone(store.getPhone());
        employee.setPermission(Integer.parseInt(SecurityLevel.Admin));
        employee.setLoginName(store.getPhone());
        String mdf5Pass= MD5Util.string2MD5(password);
        employee.setPassword(mdf5Pass);
        return employee;
    }
}
