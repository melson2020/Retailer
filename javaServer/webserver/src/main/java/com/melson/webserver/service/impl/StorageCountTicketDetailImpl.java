package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.Result;
import com.melson.webserver.dao.IProductBatchDao;
import com.melson.webserver.dao.IProductStorageDao;
import com.melson.webserver.dao.IStorageCountTicketDetailDao;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.entity.StorageCountTicket;
import com.melson.webserver.entity.StorageCountTicketDetail;
import com.melson.webserver.service.IProductBatch;
import com.melson.webserver.service.IProductStorage;
import com.melson.webserver.service.IStorageCountTicket;
import com.melson.webserver.service.IStorageCountTicketDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/23
 */
@Service
public class StorageCountTicketDetailImpl extends AbstractService<StorageCountTicketDetail> implements IStorageCountTicketDetail {
    private final IStorageCountTicketDetailDao storageCountTicketDetailDao;
    private final IStorageCountTicket storageCountTicketService;
    private final IProductStorageDao productStorageDao;
    //    private final IProductBatchDao productBatchDao;
    private final IProductBatch productBatchService;

//

    public StorageCountTicketDetailImpl(IStorageCountTicketDetailDao storageCountTicketDetailDao, IStorageCountTicket storageCountTicketService, IProductStorageDao productStorageDao, IProductBatch productBatchService) {
        this.storageCountTicketDetailDao = storageCountTicketDetailDao;
        this.storageCountTicketService = storageCountTicketService;
        this.productStorageDao = productStorageDao;
        this.productBatchService = productBatchService;
    }

    @Override
    public JpaRepository<StorageCountTicketDetail, String> getRepository() {
        return storageCountTicketDetailDao;
    }

    @Override
    public Result SaveDetailWithCountedList(List<ProductStorageDto> dtoList, StorageCountTicket ticket) {
        Result result = new Result();
        Map<Integer, List<ProductStorageDto>> dtoMap = new HashMap<>();
        //获取商户库存和批次库存表，以备用
        List<ProductStorage> storageList;
        switch (ticket.getProductType().toUpperCase()) {
            case "NORMAL":
                storageList = productStorageDao.findByStoreCodeAndSearchType(ticket.getStoreCode(), ticket.getProductType());
                break;
            case "ALL":
                storageList = productStorageDao.findByStoreCode(ticket.getStoreCode());
                break;
            default:
                storageList = productStorageDao.findByStoreCodeAndCountGreaterThan(ticket.getStoreCode(), 0);
        }
        List<ProductBatch> batchList = productBatchService.FindByStoreCodeAndFinished(ticket.getStoreCode());
        Map<Integer, ProductStorage> productStorageMap = new HashMap<>(storageList.size());
        for (ProductStorage storage : storageList) {
            productStorageMap.put(storage.getProductId(), storage);
        }
        Map<String, ProductBatch> productBatchMap = new HashMap<>(batchList.size());
        for (ProductBatch batch : batchList) {
            String key = batch.getProductId() + batch.getBatchNo();
            productBatchMap.put(key, batch);
        }
        List<ProductBatch> saveBatchList = new ArrayList<>();
        List<ProductStorage> saveStorageList = new ArrayList<>();
        //批次号位空的数据 需要自动添加批次记录
        List<ProductStorageDto> emptyBatchNoList = new ArrayList<>();
        for (ProductStorageDto dto : dtoList) {
            //添加需要更新的库存
            ProductStorage needToUpdateStorage = productStorageMap.get(dto.getProductId());
            if (needToUpdateStorage.getCount() != dto.getTotalCounted()) {
                needToUpdateStorage.setCount(dto.getTotalCounted());
                saveStorageList.add(needToUpdateStorage);
            }
            if (!StringUtils.isEmpty(dto.getBatchNo())) {
                //添加需要更新的库存批次
                ProductBatch needToUpdateBatch = productBatchMap.get(dto.getProductId() + dto.getBatchNo());
                needToUpdateBatch.setCount(dto.getCounted());
                saveBatchList.add(needToUpdateBatch);
            } else {
                emptyBatchNoList.add(dto);
            }
            List<ProductStorageDto> existList = dtoMap.get(dto.getProductId());
            if (existList == null) {
                existList = new ArrayList<>();
                existList.add(dto);
                dtoMap.put(dto.getProductId(), existList);
            } else {
                existList.add(dto);
            }
        }

        List<ProductStorage> savedP = productStorageDao.saveAll(saveStorageList);
        List<ProductBatch> savedB = productBatchService.SaveAll(saveBatchList);
        List<StorageCountTicketDetail> saveDetails = CreateDetailsWithMap(dtoMap, ticket);
        List<StorageCountTicketDetail> savedS = storageCountTicketDetailDao.saveAll(saveDetails);
        if (savedP != null && savedB != null && saveDetails != null && savedS != null) {
            //创建新批次
            if (emptyBatchNoList.size() > 0) {
                List<ProductBatch> savedEmptyPbList = productBatchService.SaveCountBatchList(emptyBatchNoList, ticket);
                //返回状态为2  有新批次增加 返回新批次
                result.setResultStatus(2);
                result.setData(savedEmptyPbList);
                ticket.setStatus(4);
            } else {
                //返回状态为1  更新成功
                result.setResultStatus(1);
                ticket.setStatus(5);
            }
            storageCountTicketService.SaveTicket(ticket);
        } else {
            result.setResultStatus(-1);
            result.setMessage("update storage fail");
        }
        return result;
    }

    private List<StorageCountTicketDetail> CreateDetailsWithMap(Map<Integer, List<ProductStorageDto>> dtoMap, StorageCountTicket ticket) {
        if (dtoMap.size() <= 0) return null;
        List<StorageCountTicketDetail> saveDetails = new ArrayList<>();
        for (Integer productId : dtoMap.keySet()) {
            List<ProductStorageDto> dtoList = dtoMap.get(productId);
            boolean onlyChangedTotal = true;
            for (ProductStorageDto dto : dtoList) {
                if (dto.getCounted() - dto.getCount() != 0) {
                    onlyChangedTotal = false;
                }
            }
            if (onlyChangedTotal) {
                StorageCountTicketDetail detail = CreateDetail(dtoList.get(0), ticket);
                saveDetails.add(detail);
            } else {
                for (ProductStorageDto dto : dtoList) {
                    StorageCountTicketDetail detail = CreateDetail(dto, ticket);
                    saveDetails.add(detail);
                }
            }
        }
        return saveDetails;
    }

    private StorageCountTicketDetail CreateDetail(ProductStorageDto dto, StorageCountTicket ticket) {
        StorageCountTicketDetail detail = new StorageCountTicketDetail();
        detail.setProductId(dto.getProductId());
        detail.setStoreCode(ticket.getStoreCode());
        detail.setProductName(dto.getProductName());
        if (dto.getCounted() == dto.getCount()) {
            String type = dto.getTotalCounted() > dto.getTotalCount() ? "plus" : "minus ";
            detail.setType(type);
            Integer count = dto.getTotalCounted() - dto.getTotalCount();
            detail.setCount(count < 0 ? count * -1 : count);
            detail.setTotalCountChange(1);
        } else {
            String type = dto.getCounted() > dto.getCount() ? "plus" : "minus ";
            detail.setType(type);
            Integer count = dto.getCounted() - dto.getCount();
            detail.setCount(count < 0 ? count * -1 : count);
            detail.setBatchNo(dto.getBatchNo());
            detail.setTotalCountChange(0);
        }
        detail.setTicketCode(ticket.getCode());
        return detail;
    }
}