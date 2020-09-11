package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.Vo.StorageAndProductCountVo;
import com.melson.webserver.dao.IProductBatchDao;
import com.melson.webserver.dao.IProductDao;
import com.melson.webserver.dao.IProductStorageDao;
import com.melson.webserver.entity.Product;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.service.IProductStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@Service
public class ProductStorageImpl extends AbstractService<ProductStorage> implements IProductStorage {
    private final IProductStorageDao productStorageDao;
    private final IProductDao productDao;
    private final IProductBatchDao productBatchDao;

    public ProductStorageImpl(IProductStorageDao productStorageDao, IProductDao productDao, IProductBatchDao productBatchDao) {
        this.productStorageDao = productStorageDao;
        this.productDao = productDao;
        this.productBatchDao = productBatchDao;
    }

    @Override
    public JpaRepository<ProductStorage, String> getRepository() {
        return productStorageDao;
    }

    @Override
    public StorageAndProductCountVo GetProductAndStorageCount(String storeCode) {
        StorageAndProductCountVo vo=new StorageAndProductCountVo();
        Integer productCount=productDao.GetCountWithStore(storeCode);
        Integer storageCount=productStorageDao.GetCountWithStore(storeCode);
        vo.setProductCount(productCount);
        vo.setStorageCount(storageCount);
        return vo;
    }

    @Override
    public List<ProductStorage> GenerateStorage(String storeCode) {
        List<Product> productList=productDao.findByStoreCode(storeCode);
        List<ProductStorage> storageList=new ArrayList<>(productList.size());
        for (Product p:productList){
            ProductStorage storage=new ProductStorage();
            storage.setCount(0);
            storage.setProductId(p.getId());
            storage.setStoreCode(storeCode);
            storage.setProductName(p.getName());
            storage.setProductType(p.getType());
            storage.setProductSpecification(p.getSpecification());
            storage.setUnit(p.getUnit());
            storageList.add(storage);
        }
        if(storageList.size()>0){
          return productStorageDao.saveAll(storageList);
        }else {
            return null;
        }
    }

    @Override
    public List<ProductStorage> FindAll(String storeCode) {
        return productStorageDao.findByStoreCode(storeCode);
    }

    @Override
    public List<ProductBatch> FindBatchList(String storeCode, Integer productId) {
        return productBatchDao.findByStoreCodeAndProductIdAndFinished(storeCode,productId,0);
    }
}
