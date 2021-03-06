package com.melson.base.service;

import com.melson.base.IService;
import com.melson.base.entity.StoreEmployee;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
public interface IStoreEmployee extends IService<StoreEmployee> {
    List<StoreEmployee> findAll();
    StoreEmployee Login(StoreEmployee employee);
    List<StoreEmployee> findByStoreCode(String stroeCode);
    StoreEmployee CreateEmployee(StoreEmployee employee);
    StoreEmployee findByLoginName(String loginName);
    Integer UpdateEmployee(StoreEmployee employee);
    Integer DeleteEmployee(StoreEmployee employee);
    Integer RetSetPassword(String userId,String loginName,String oldPass,String newPass);
}
