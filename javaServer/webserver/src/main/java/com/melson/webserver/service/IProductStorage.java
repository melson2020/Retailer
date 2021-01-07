package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.Vo.ShareStorageVo;
import com.melson.webserver.Vo.StorageAndProductCountVo;
import com.melson.webserver.Vo.StorageRecordVo;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;

import java.util.Date;
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
    List<ProductStorage> FindStorageListWithType(String store,String productType);
    List<ProductStorageDto> FindWithProductType(String storeCode, String productType);
    List<ProductBatch> FindBatchList(String storeCode,Integer productId);
    List<StorageRecordVo> FindProductStorageRec(Integer productId, String startDate, String endDate, String storeCode);
    ProductStorage FindByProductIdAndStoreCode(Integer id, String storeCode);
    List<ProductStorage> findStorageAndBatchByPName(String productNmae,String storeCode);

    List<ShareStorageVo> FindShareStorage(String shareCode);
}
