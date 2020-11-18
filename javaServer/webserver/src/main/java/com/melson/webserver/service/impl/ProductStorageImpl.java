package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.Vo.StorageAndProductCountVo;
import com.melson.webserver.dao.IProductBatchDao;
import com.melson.webserver.dao.IProductDao;
import com.melson.webserver.dao.IProductStorageDao;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.Product;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.service.IProductStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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
        StorageAndProductCountVo vo = new StorageAndProductCountVo();
        Integer productCount = productDao.GetCountWithStore(storeCode);
        Integer storageCount = productStorageDao.GetCountWithStore(storeCode);
        vo.setProductCount(productCount);
        vo.setStorageCount(storageCount);
        return vo;
    }

    @Override
    public List<ProductStorage> GenerateStorage(String storeCode) {
        List<Product> productList = productDao.findByStoreCode(storeCode);
        List<ProductStorage> storageList = new ArrayList<>(productList.size());
        for (Product p : productList) {
            ProductStorage storage = new ProductStorage();
            storage.setCount(0);
            storage.setProductId(p.getId());
            storage.setStoreCode(storeCode);
            storage.setProductName(p.getName());
            storage.setProductType(p.getType());
            storage.setProductSpecification(p.getSpecification());
            storage.setUnit(p.getUnit());
            storageList.add(storage);
        }
        if (storageList.size() > 0) {
            return productStorageDao.saveAll(storageList);
        } else {
            return null;
        }
    }

    @Override
    public List<ProductStorage> FindAll(String storeCode) {
        return productStorageDao.findByStoreCode(storeCode);
    }

    @Override
    public List<ProductStorage> FindStorageListWithType(String storeCode, String productType) {
        switch (productType.toUpperCase()) {
            case "ALL":
                return productStorageDao.findByStoreCode(storeCode);
            case "NORMAL":
                return productStorageDao.findByStoreCodeAndSearchType(storeCode, productType);
            default:
                return productStorageDao.findByStoreCodeAndCountGreaterThan(storeCode, 0);

        }
    }

    @Override
    public List<ProductStorageDto> FindWithProductType(String storeCode, String searchType) {
        List<ProductBatch> batchList = productBatchDao.findByStoreCodeAndFinished(storeCode, 0);
        List<ProductStorage> storageList;
        switch (searchType.toUpperCase()) {
            case "ALL":
                storageList = productStorageDao.findByStoreCode(storeCode);
                break;
            case "NORMAL":
                storageList = productStorageDao.findByStoreCodeAndSearchType(storeCode, searchType);
                break;
            default:
                storageList = productStorageDao.findByStoreCodeAndCountGreaterThan(storeCode, 0);
                break;
        }
        List<ProductStorageDto> dtoList = GenerateCountList(storageList, batchList);
        dtoList.sort(new Comparator<ProductStorageDto>() {
            @Override
            public int compare(ProductStorageDto o1, ProductStorageDto o2) {
                return o1.getProductId() < o2.getProductId() ? 0 : 1;
            }
        });
        return dtoList;
    }


    private List<ProductStorageDto> GenerateCountList(List<ProductStorage> productStorageList, List<ProductBatch> allBatchList) {
        Map<Integer, List<ProductBatch>> productBatchMap = new HashMap<>();
        List<ProductStorageDto> dtoList = new ArrayList<>();
        for (ProductBatch batch : allBatchList) {
            List<ProductBatch> existList = productBatchMap.get(batch.getProductId());
            if (existList == null) {
                existList = new ArrayList<>();
                existList.add(batch);
                productBatchMap.put(batch.getProductId(), existList);
            } else {
                existList.add(batch);
            }
        }
        for (ProductStorage storage : productStorageList) {
            List<ProductBatch> batchList = productBatchMap.get(storage.getProductId());
            if (batchList == null || batchList.size() <= 0) {
                ProductStorageDto dto = CreateDto(storage, null, 1);
                dtoList.add(dto);
            } else {
                for (ProductBatch batch : batchList) {
                    ProductStorageDto dto = CreateDto(storage, batch, batchList.size());
                    dtoList.add(dto);
                }
            }
        }
        return dtoList;
    }

    private ProductStorageDto CreateDto(ProductStorage storage, ProductBatch batch, Integer spanCount) {
        ProductStorageDto dto = new ProductStorageDto();
        dto.setProductId(storage.getProductId());
        dto.setProductName(storage.getProductName());
        dto.setType(storage.getProductType());
        dto.setSpecification(storage.getProductSpecification());
        dto.setTotalCount(storage.getCount());
        dto.setSpanCount(spanCount);
        if (batch == null) {
            dto.setCount(storage.getCount());
            dto.setCountUnit(storage.getUnit());
        } else {
            dto.setBatchNo(batch.getBatchNo());
            dto.setSupplyName(batch.getSupplyName());
            dto.setSupplyId(batch.getSupplyId());
            dto.setCount(batch.getCount());
            dto.setCountUnit(batch.getCountUnit());
        }
        return dto;
    }

    @Override
    public List<ProductBatch> FindBatchList(String storeCode, Integer productId) {
        return productBatchDao.findByStoreCodeAndProductIdAndFinished(storeCode, productId, 0);
    }

    @Override
    public ProductStorage FindByProductIdAndStoreCode(Integer id, String storeCode) {
        ProductStorage storage = productStorageDao.findByProductIdAndStoreCode(id, storeCode);
        return storage;
    }
}
