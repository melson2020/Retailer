package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.Vo.StorageAndProductCountVo;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
public interface IProductStorage extends IService<ProductStorage> {
    StorageAndProductCountVo GetProductAndStorageCount(String storeCode);
    List<ProductStorage> GenerateStorage(String storeCode);
    List<ProductStorage> FindAll(String storeCode);
    List<ProductBatch> FindBatchList(String storeCode,Integer productId);
}
