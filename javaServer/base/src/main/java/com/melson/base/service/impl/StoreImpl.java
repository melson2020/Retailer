package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.cache.CacheKey;
import com.melson.base.cache.CacheUtil;
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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class StoreImpl extends AbstractService<Store> implements IStore {
    private final IStoreDao storeDao;
    private final IStoreEmployeeDao storeEmployeeDao;
    private final CacheUtil cacheUtil;

    public StoreImpl(IStoreDao storeDao,IStoreEmployeeDao storeEmployeeDao,CacheUtil cacheUtil) {
        this.storeDao = storeDao;
        this.storeEmployeeDao=storeEmployeeDao;
        this.cacheUtil=cacheUtil;
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
            StoreEmployee saveEP=storeEmployeeDao.save(employee);
            if(saveEP!=null) {
                //添加缓存
                Map<String, StoreEmployee> map = cacheUtil.GetObjectValue(CacheKey.StoreEmployee, Map.class);
                map.put(saveEP.getUserId(), saveEP);
            }
            return storeEmployeeDao.save(employee) != null;
        }else {
            return false;
        }
    }

    @Override
    public boolean CheckPhone(String phone) {
         String phoneNum=storeDao.findByPhone(phone);
         return StringUtils.isEmpty(phoneNum);
    }

    @Override
    public Store findByCode(String code) {
        return storeDao.findByCode(code);
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
        employee.setCreateDate(new Date());
        return employee;
    }
}
