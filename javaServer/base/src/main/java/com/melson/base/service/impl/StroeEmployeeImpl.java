package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.IStoreEmployeeDao;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.service.IStoreEmployee;
import com.melson.base.utils.MD5Util;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        String md5Pass = MD5Util.string2MD5(employee.getPassword());
        StoreEmployee existEmployee = storeEmployeeDao.findByLoginNameAndPassword(employee.getLoginName(), md5Pass);
        return existEmployee;
    }

    @Override
    public List<StoreEmployee> findByStoreCode(String stroeCode) {
        return storeEmployeeDao.findAllByStoreCode(stroeCode);
    }

    @Override
    public StoreEmployee CreateEmployee(StoreEmployee employee) {
        String md5Pass = MD5Util.string2MD5(employee.getPassword());
        employee.setPassword(md5Pass);
        employee.setUserId(UUID.randomUUID().toString());
        employee.setCreateDate(new Date());
        if(employee.getPermission()==null||employee.getPermission()<=0){
            employee.setPermission(1);
        }
        StoreEmployee saved = storeEmployeeDao.save(employee);
        return saved;
    }

    @Override
    public StoreEmployee findByLoginName(String loginName) {
        return storeEmployeeDao.findByLoginName(loginName);
    }

    @Override
    @Transactional
    public Integer UpdateEmployee(StoreEmployee employee) {
        return storeEmployeeDao.updateEmployee(employee.getUserName(),employee.getPhone(),employee.getGender(),employee.getPermission(),employee.getUserId());
    }

    @Override
    @Transactional
    public Integer DeleteEmployee(StoreEmployee employee) {
        return storeEmployeeDao.deleteByUserId(employee.getUserId());
    }
}
