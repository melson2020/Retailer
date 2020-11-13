package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.Result;
import com.melson.webserver.dao.IProductStorageDao;
import com.melson.webserver.dao.IStorageCountTicketDetailDao;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.entity.StorageCountTicket;
import com.melson.webserver.entity.StorageCountTicketDetail;
import com.melson.webserver.service.IProductBatch;
import com.melson.webserver.service.IStorageCountTicket;
import com.melson.webserver.service.IStorageCountTicketDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
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
        //把DTO 按照productId 分组
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
        //库存Map 以便获取需要更新的库存
        Map<Integer, ProductStorage> productStorageMap = new HashMap<>(storageList.size());
        for (ProductStorage storage : storageList) {
            productStorageMap.put(storage.getProductId(), storage);
        }
        //批次库存，以便获取需要更新的批次
        Map<String, ProductBatch> productBatchMap = new HashMap<>(batchList.size());
        //已存在的批次map 防止添加批次时重复添加 key 值为入库单号+产品ID
        Map<String, ProductBatch> existBatchMap = new HashMap<>();
        for (ProductBatch batch : batchList) {
            String key = batch.getProductId() + batch.getBatchNo();
            productBatchMap.put(key, batch);
            existBatchMap.put(batch.getProductId() + batch.getStorageInCode(), batch);
        }
        List<ProductBatch> saveBatchList = new ArrayList<>();
        List<ProductStorage> saveStorageList = new ArrayList<>();
        //批次号为空的数据 需要自动添加批次记录
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
                if(dto.getCounted()<=0){
                  needToUpdateBatch.setFinished(1);
                }
                saveBatchList.add(needToUpdateBatch);
            } else {
                emptyBatchNoList.add(dto);
            }
            //dto 分组
            List<ProductStorageDto> existList = dtoMap.get(dto.getProductId());
            if (existList == null) {
                existList = new ArrayList<>();
                existList.add(dto);
                dtoMap.put(dto.getProductId(), existList);
            } else {
                existList.add(dto);
            }
        }
        //用于存储新建批次号
        Map<String, String> createdProductBatchNoMap = new HashMap<>();
        //创建新批次 添加前需要筛选出已创建批次记录 防止重复添加
        if (emptyBatchNoList.size() > 0) {
            List<ProductBatch> savedEmptyPbList = productBatchService.SaveCountBatchList(emptyBatchNoList, ticket, existBatchMap);
            //初始化新建批次号MAP 收集新建的批次号 以便后续追加至 盘点详细中
            for (ProductBatch batch : savedEmptyPbList) {
                if (createdProductBatchNoMap.get(batch.getStorageInCode()) == null) {
                    createdProductBatchNoMap.put(batch.getStorageInCode(), batch.getBatchNo());
                }
            }
            //返回状态为2  有新批次增加 返回新批次
            result.setResultStatus(2);
            result.setData(savedEmptyPbList);
        }
        List<StorageCountTicketDetail> eixtDetails = storageCountTicketDetailDao.findByTicketCode(ticket.getCode());
        List<ProductStorage> savedP = productStorageDao.saveAll(saveStorageList);
        List<ProductBatch> savedB = productBatchService.SaveAll(saveBatchList);
        //以DTO 分组创建盘点详细 去重防止重复插入盘点详细详细
        List<StorageCountTicketDetail> saveDetails = CreateDetailsWithMap(dtoMap, ticket, createdProductBatchNoMap, eixtDetails);
        List<StorageCountTicketDetail> savedS = storageCountTicketDetailDao.saveAll(saveDetails);
        if (savedP == null || savedB == null || saveDetails == null || savedS == null) {
            result.setResultStatus(-1);
            ticket.setStatus(3);
            result.setMessage("update storage fail");
        } else {
            if (emptyBatchNoList.size() > 0) {
                ticket.setStatus(4);
            } else {
                ticket.setStatus(5);
                ticket.setResult("complete");
            }
        }
        storageCountTicketService.SaveTicket(ticket);
        return result;
    }

    @Override
    public List<StorageCountTicketDetail> FindCountTicketDetails(String ticketCode) {
      return storageCountTicketDetailDao.findByTicketCode(ticketCode);
    }

    /**
     * 创建需要保存的盘点详细， 具备去重功能 防止重复插入
     *
     * @param dtoMap        按照productId 分组的dto
     * @param ticket        盘点单
     * @param newBatchNoMap 新建批次Map key: ticketCode value:batchNo
     * @param eixtDetails   此ticket 已存在的盘点详细
     * @return 需要保存的盘点详细
     */
    private List<StorageCountTicketDetail> CreateDetailsWithMap(Map<Integer, List<ProductStorageDto>> dtoMap, StorageCountTicket ticket, Map<String, String> newBatchNoMap, List<StorageCountTicketDetail> eixtDetails) {
        if (dtoMap.size() <= 0) return null;
        Map<Integer, StorageCountTicketDetail> detailMap = new HashMap<>(eixtDetails.size());
        for (StorageCountTicketDetail detail : eixtDetails) {
            detailMap.put(detail.getProductId(), detail);
        }
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
                //只有总数变化时 生成一条记录
                StorageCountTicketDetail detail = CreateOnlyTotalChangedDeatil(dtoList.get(0), ticket, detailMap.get(productId));
                saveDetails.add(detail);
            } else {
                for (ProductStorageDto dto : dtoList) {
                    StorageCountTicketDetail detail = CreateDetail(dto, ticket, newBatchNoMap.get(ticket.getCode()), detailMap.get(productId));
                    saveDetails.add(detail);
                }
            }
        }
        return saveDetails;
    }

    //当只有总数变化时
    private StorageCountTicketDetail CreateOnlyTotalChangedDeatil(ProductStorageDto dto, StorageCountTicket ticket, StorageCountTicketDetail existDetail) {
        if (existDetail == null) {
            existDetail = new StorageCountTicketDetail();
        }
        existDetail.setProductId(dto.getProductId());
        existDetail.setStoreCode(ticket.getStoreCode());
        existDetail.setProductName(dto.getProductName());
        String type = dto.getTotalCounted() > dto.getTotalCount() ? "plus" : "minus ";
        existDetail.setType(type);
        Integer count = dto.getTotalCounted() - dto.getTotalCount();
        existDetail.setCount(count < 0 ? count * -1 : count);
        existDetail.setTotalCountChange(1);
        return existDetail;
    }

    private StorageCountTicketDetail CreateDetail(ProductStorageDto dto, StorageCountTicket ticket, String newBatchNo, StorageCountTicketDetail existDetail) {
        if (existDetail == null) existDetail = new StorageCountTicketDetail();
        existDetail.setProductId(dto.getProductId());
        existDetail.setStoreCode(ticket.getStoreCode());
        existDetail.setProductName(dto.getProductName());
        String type = dto.getCounted() > dto.getCount() ? "plus" : "minus ";
        existDetail.setType(type);
        Integer count = dto.getCounted() - dto.getCount();
        existDetail.setCount(count < 0 ? count * -1 : count);
        if (!StringUtils.isEmpty(dto.getBatchNo())) {
            existDetail.setBatchNo(dto.getBatchNo());
        } else {
            existDetail.setBatchNo(newBatchNo);
        }
        existDetail.setTotalCountChange(0);
        existDetail.setTicketCode(ticket.getCode());
        return existDetail;
    }
}