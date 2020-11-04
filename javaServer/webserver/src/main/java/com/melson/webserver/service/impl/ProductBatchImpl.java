package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IProductBatchDao;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.StorageCountTicket;
import com.melson.webserver.service.IProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@Service
public class ProductBatchImpl extends AbstractService<ProductBatch> implements IProductBatch {
    private final IProductBatchDao productBatchDao;

    public ProductBatchImpl(IProductBatchDao productBatchDao) {
        this.productBatchDao = productBatchDao;
    }

    @Override
    public JpaRepository<ProductBatch, String> getRepository() {
        return productBatchDao;
    }

    @Override
    public List<ProductBatch> SaveCountBatchList(List<ProductStorageDto> dtoList, StorageCountTicket ticket) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String batchNo=sdf.format(new Date());
        List<ProductBatch> saveBatchList=new ArrayList<>();
        for(ProductStorageDto dto:dtoList){
              saveBatchList.add(CreateProductBatch(dto,ticket.getStoreCode(),batchNo,ticket.getCode()));
        }
        return productBatchDao.saveAll(saveBatchList);
    }

    @Override
    public List<ProductBatch> FindByStoreCodeAndFinished(String storeCode) {
        return productBatchDao.findByStoreCodeAndFinished(storeCode,0);
    }

    @Override
    public List<ProductBatch> SaveAll(List<ProductBatch> productBatchList) {
        return productBatchDao.saveAll(productBatchList);
    }

    @Override
    public List<ProductBatch> FindBatchListForUpdate(String storeCode,String ticketCode) {
        return productBatchDao.findByStoreCodeAndBatchTypeAndStorageInCodeAndSupplyIdIsNull(storeCode,"C",ticketCode);
    }

    private ProductBatch CreateProductBatch(ProductStorageDto dto,String storeCode,String batchNo,String ticketCode){
        ProductBatch batch=new ProductBatch();
        batch.setStoreCode(storeCode);
        batch.setProductId(dto.getProductId());
        batch.setBatchNo(batchNo);
        batch.setCount(dto.getCounted());
        batch.setStorageInCode(ticketCode);
        batch.setCountUnit(dto.getCountUnit());
        batch.setBatchType("C");
        batch.setProductName(dto.getProductName());
        return batch;
    }
}
