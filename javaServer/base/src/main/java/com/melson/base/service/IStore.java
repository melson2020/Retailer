package com.melson.base.service;

import com.melson.base.IService;
import com.melson.base.entity.Store;

/**
 * Created by Nelson on 2020/7/23.
 */
public interface IStore extends IService<Store> {
    boolean RegisterStore(Store store,String password);
    boolean CheckPhone(String phone);
    Store findByCode(String code);
}
