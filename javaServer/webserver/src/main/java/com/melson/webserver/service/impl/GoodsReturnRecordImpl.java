package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.Result;
import com.melson.webserver.dao.*;
import com.melson.webserver.entity.*;
import com.melson.webserver.service.IGoodsReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/16
 */
@Service
public class GoodsReturnRecordImpl extends AbstractService<GoodsReturnRecord> implements IGoodsReturnRecord {
    private final IGoodsReturnRecordDao goodsReturnRecordDao;
    private final IStorageOutBillDetailDao billDetailDao;
    private final IProductBatchDao productBatchDao;
    private final IProductStorageDao productStorageDao;
    private final IStorageOutTicketDao outTicketDao;
    private final IStorageOutDetailDao outDetailDao;

    public GoodsReturnRecordImpl(IGoodsReturnRecordDao goodsReturnRecordDao, IStorageOutBillDetailDao billDetailDao, IProductBatchDao productBatchDao, IProductStorageDao productStorageDao, IStorageOutTicketDao outTicketDao, IStorageOutDetailDao outDetailDao) {
        this.goodsReturnRecordDao = goodsReturnRecordDao;
        this.billDetailDao = billDetailDao;
        this.productBatchDao = productBatchDao;
        this.productStorageDao = productStorageDao;
        this.outTicketDao = outTicketDao;
        this.outDetailDao = outDetailDao;
    }

    @Override
    public JpaRepository<GoodsReturnRecord, String> getRepository() {
        return goodsReturnRecordDao;
    }

    @Override
    @Transactional
    public Result SaveGoodsReturnRecords(List<GoodsReturnRecord> returnRecords, List<StorageOutDetail> outDetails) {
        Result result = new Result();
        String billCode = returnRecords.get(0).getBillCode();
        List<StorageOutBillDetail> billDetails = billDetailDao.findByOutBillCode(billCode);
        if (billDetails == null || billDetails.size() <= 0) {
            result.setResultStatus(-1);
            result.setMessage("can find bill details");
            return result;
        }
        Map<String, StorageOutBillDetail> detailMap = new HashMap<>(billDetails.size());
        for (StorageOutBillDetail detail : billDetails) {
            detailMap.put(detail.getOutDetailCode(), detail);
        }
        Date createTime = new Date();
        //查询库存相关信息
        Set<Integer> productIds = new HashSet<>();
        Set<String> batchNos = new HashSet<>();
        boolean computed = true;
        for (GoodsReturnRecord record : returnRecords) {
            StorageOutBillDetail billDetail = detailMap.get(record.getOutDetailCode());
            productIds.add(record.getProductId());
            batchNos.add(record.getBatchNo());
            if (billDetail == null) {
                computed = false;
                continue;
            }
            record.setCreateTime(createTime);
            record.setProfitUnit(billDetail.getUnitProfit());
            BigDecimal totalProfit = billDetail.getUnitProfit().multiply(new BigDecimal(record.getCount()));
            record.setTotalProfit(totalProfit);
        }
        if (computed) {
            List<GoodsReturnRecord> saved = goodsReturnRecordDao.saveAll(returnRecords);
            //更新库存
            if (saved != null) {
                String storeCode = saved.get(0).getStoreCode();
                List<ProductBatch> productBatchList = productBatchDao.findByBatchNoInAndStoreCode(batchNos, storeCode);
                List<ProductStorage> productStorageList = productStorageDao.findByProductIdIn(productIds);
                Map<String, ProductBatch> batchMap = new HashMap<>(productBatchList.size());
                Map<Integer, ProductStorage> storageMap = new HashMap<>(productBatchList.size());
                for (ProductBatch batch : productBatchList) {
                    batchMap.put(batch.getBatchNo() + batch.getProductId(), batch);
                }
                for (ProductStorage storage : productStorageList) {
                    storageMap.put(storage.getProductId(), storage);
                }
                for (GoodsReturnRecord saveRecord : saved) {
                    ProductStorage storage = storageMap.get(saveRecord.getProductId());
                    storage.setCount(storage.getCount() + saveRecord.getCount());
                    ProductBatch batch = batchMap.get(saveRecord.getBatchNo() + saveRecord.getProductId());
                    batch.setCount(batch.getCount() + saveRecord.getCount());
                }
                productBatchDao.saveAll(batchMap.values());
                productStorageDao.saveAll(storageMap.values());
                UpdateOutTicketAndDetailStatus(outDetails);
            } else {
                result.setResultStatus(-1);
                result.setMessage("save records failed");
            }
        } else {
            result.setResultStatus(-1);
            result.setMessage("can find return back bill detail");
        }
        return result;
    }

    private void UpdateOutTicketAndDetailStatus(List<StorageOutDetail> outDetails) {
        Integer ticketStatus = 2;
        String ticketCode = outDetails.get(0).getOutTicketCode();
        String storeCode = outDetails.get(0).getStoreCode();
        for (StorageOutDetail detail : outDetails) {
            Integer returnCount = detail.getReturnCount() + detail.getBackCount();
            detail.setReturnCount(returnCount);
            if (detail.getOutCount() <= returnCount) {
                detail.setStatus(2);
            } else {
                detail.setStatus(1);
            }
            ;
            if (detail.getStatus() == null || detail.getStatus() < 2) {
                ticketStatus = 1;
            }
        }
        outDetailDao.saveAll(outDetails);
        outTicketDao.UpdateOutTicketStatus(ticketStatus, ticketCode, storeCode);
    }
}
