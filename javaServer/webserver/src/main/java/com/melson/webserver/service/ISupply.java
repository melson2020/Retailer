package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.Result;
import com.melson.base.entity.StoreEmployee;
import com.melson.webserver.entity.Supply;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
public interface ISupply extends IService<Supply> {
    List<Supply> findAll();

    List<Supply> findSupplyByStoreCode(String storeCode);

    Integer DeleteSupply(Supply supply);

    Supply Query(Supply supply);

    Supply SaveSupply(Supply supply);

    Result SaveAndUpdate(Supply supply);
}
