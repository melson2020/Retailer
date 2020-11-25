package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IProductBatchDao;
import com.melson.webserver.dao.IStorageCountTicketDetailDao;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.StorageCountTicket;
import com.melson.webserver.entity.StorageCountTicketDetail;
import com.melson.webserver.service.IProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@Service
public class ProductBatchImpl extends AbstractService<ProductBatch> implements IProductBatch {
    private final IProductBatchDao productBatchDao;
    private final IStorageCountTicketDetailDao storageCountTicketDetailDao;

    public ProductBatchImpl(IProductBatchDao productBatchDao, IStorageCountTicketDetailDao storageCountTicketDetailDao) {
        this.productBatchDao = productBatchDao;
        this.storageCountTicketDetailDao = storageCountTicketDetailDao;
    }

    @Override
    public JpaRepository<ProductBatch, String> getRepository() {
        return productBatchDao;
    }

    /**
     * 盘点时追加新批次
     *
     * @param dtoList  盘点单itemList
     * @param ticket   盘点单号
     * @param existMap 数据库中已存在未完成的批次信息 key: productId+storageInCode
     * @return 保存的批次信息
     */
    @Override
    public List<ProductBatch> SaveCountBatchList(List<ProductStorageDto> dtoList, StorageCountTicket ticket, Map<String, ProductBatch> existMap) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String batchNo = sdf.format(new Date());
        List<ProductBatch> saveBatchList = new ArrayList<>();
        for (ProductStorageDto dto : dtoList) {
            String key = dto.getProductId() + ticket.getCode();
            ProductBatch existBatch = existMap.get(key);
            if (existBatch == null) {
                saveBatchList.add(CreateProductBatch(dto, ticket.getStoreCode(), batchNo, ticket.getCode()));
            } else {
                existBatch.setCount(dto.getCounted());
                saveBatchList.add(existBatch);
            }
        }
        List<ProductBatch> saved = productBatchDao.saveAll(saveBatchList);
        return saved;
    }

    @Override
    public List<ProductBatch> FindByStoreCodeAndFinished(String storeCode) {
        return productBatchDao.findByStoreCodeAndFinished(storeCode, 0);
    }

    @Override
    @Transactional
    public List<ProductBatch> SaveAll(List<ProductBatch> productBatchList) {
        //更新盘点单详细供应商名称
        if (productBatchList.size() > 0) {
            List<StorageCountTicketDetail> emptysupplyNameList = storageCountTicketDetailDao.findByStoreCodeAndSupplyNameIsNull(productBatchList.get(0).getStoreCode());
            if (emptysupplyNameList.size() > 0) {
                Map<String, StorageCountTicketDetail> detailMap = new HashMap<>(emptysupplyNameList.size());
                for (StorageCountTicketDetail detail : emptysupplyNameList) {
                    String key = detail.getProductId() + detail.getBatchNo();
                    detailMap.put(key, detail);
                }
                List<StorageCountTicketDetail> detailUpdateList = new ArrayList<>();
                for (ProductBatch batch : productBatchList) {
                    String key = batch.getProductId() + batch.getBatchNo();
                    StorageCountTicketDetail existDetail = detailMap.get(key);
                    if (existDetail != null) {
                        existDetail.setSupplyName(batch.getSupplyName());
                        detailUpdateList.add(existDetail);
                    }
                }
                if (detailUpdateList.size() > 0) {
                    storageCountTicketDetailDao.saveAll(detailUpdateList);
                }
            }
        }
        return productBatchDao.saveAll(productBatchList);
    }

    @Override
    public List<ProductBatch> FindBatchListForUpdate(String storeCode, String ticketCode) {
        return productBatchDao.findByStoreCodeAndBatchTypeAndStorageInCodeAndSupplyIdIsNull(storeCode, "C", ticketCode);
    }

    private ProductBatch CreateProductBatch(ProductStorageDto dto, String storeCode, String batchNo, String ticketCode) {
        ProductBatch batch = new ProductBatch();
        batch.setStoreCode(storeCode);
        batch.setProductId(dto.getProductId());
        batch.setBatchNo(batchNo);
        batch.setCount(dto.getCounted());
        batch.setStorageInCode(ticketCode);
        batch.setCountUnit(dto.getCountUnit());
        batch.setTaxRate("");
        batch.setVat(0);
        batch.setFinished(batch.getCount() == 0 ? 1 : 0);
        batch.setBatchType("C");
        batch.setProductName(dto.getProductName());
        return batch;
    }
}
