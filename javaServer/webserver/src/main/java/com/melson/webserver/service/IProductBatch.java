package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.StorageCountTicket;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
public interface IProductBatch extends IService<ProductBatch> {
    List<ProductBatch> SaveCountBatchList(List<ProductStorageDto> dtoList, StorageCountTicket ticket);
    List<ProductBatch> FindByStoreCodeAndFinished(String storeCode);
    List<ProductBatch> SaveAll(List<ProductBatch> productBatchList);
    List<ProductBatch> FindBatchListForUpdate(String storeCode,String ticketCode);
}
