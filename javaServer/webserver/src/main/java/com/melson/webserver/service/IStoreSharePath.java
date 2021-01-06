package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.entity.StoreSharePath;

/**
 * @Author Nelson
 * @Description
 * @Date 2021/1/6
 */
public interface IStoreSharePath extends IService<StoreSharePath> {
     StoreSharePath CreateSharePath(String storeCode,String parentPath);
     StoreSharePath FindSharePath(String storeCode);

    StoreSharePath FindSharePathWithShareCode(String shareCode);
}
