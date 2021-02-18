package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.Result;
import com.melson.webserver.dao.*;
import com.melson.webserver.entity.*;
import com.melson.webserver.service.IGoodsReturnRecord;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
//            List<GoodsReturnRecord> saved = goodsReturnRecordDao.saveAll(returnRecords);
            //更新库存
            if (returnRecords.size() > 0) {
                String storeCode = returnRecords.get(0).getStoreCode();
                List<ProductBatch> productBatchList = productBatchDao.findByBatchNoInAndStoreCode(batchNos, storeCode);
                List<ProductStorage> productStorageList = productStorageDao.findByProductIdIn(productIds);
                Map<String, ProductBatch> batchMap = new HashMap<>(productBatchList.size());
                Map<Integer, ProductStorage> storageMap = new HashMap<>(productBatchList.size());
                for (ProductBatch batch : productBatchList) {
                    batchMap.put(batch.getBatchNo() + batch.getProductId()+batch.getSupplyId()+batch.getId(), batch);
                }
                for (ProductStorage storage : productStorageList) {
                    storageMap.put(storage.getProductId(), storage);
                }
                for (GoodsReturnRecord saveRecord : returnRecords) {
                    ProductStorage storage = storageMap.get(saveRecord.getProductId());
                    saveRecord.setBeforeCount(storage.getCount());
                    storage.setCount(storage.getCount() + saveRecord.getCount());
                    saveRecord.setAfterCount(storage.getCount());
                    ProductBatch batch = batchMap.get(saveRecord.getBatchNo() + saveRecord.getProductId()+saveRecord.getSupplyId()+saveRecord.getBatchId());
                    batch.setCount(batch.getCount() + saveRecord.getCount());
                    batch.setFinished(0);
                }
               List<GoodsReturnRecord> savedRecords=  goodsReturnRecordDao.saveAll(returnRecords);
                productBatchDao.saveAll(batchMap.values());
                productStorageDao.saveAll(storageMap.values());
                UpdateOutTicketAndDetailStatus(outDetails);
                BigDecimal totalReturnPrice=new BigDecimal(0.0);
                for (GoodsReturnRecord gr:savedRecords){
                    totalReturnPrice=totalReturnPrice.add(gr.getTotalPrice());
                }
                result.setMessage(totalReturnPrice.toString());
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

    @Override
    public List<GoodsReturnRecord> FindRecords(String storeCode,String startDate,String endDate,String customerId,String productId,String employeeId) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date begin=sdf.parse(startDate);
            Date end=sdf.parse(endDate);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(end);
            calendar.add(Calendar.DATE,1);
            Date newEnd=calendar.getTime();
            List<GoodsReturnRecord> recordList=goodsReturnRecordDao.findAll(new Specification<GoodsReturnRecord>(){
                @Nullable
                @Override
                public Predicate toPredicate(Root<GoodsReturnRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Predicate predicate=criteriaBuilder.conjunction();
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("storeCode"),storeCode));
                    predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"),begin));
                    predicate.getExpressions().add(criteriaBuilder.lessThan(root.get("createTime"),newEnd));
                    if(!StringUtils.isEmpty(customerId)){
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("customerId"),Integer.parseInt(customerId)));
                    }
                    if(!StringUtils.isEmpty(productId)){
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("productId"),Integer.parseInt(productId)));
                    }
                    if(!StringUtils.isEmpty(employeeId)){
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("operationEmployeeUserId"),employeeId));
                    }
                    Predicate predicate1 = predicate;
                    return predicate1;
                }
            });
            return recordList;
        }catch (Exception e){
            return null;
        }
    }

    private void UpdateOutTicketAndDetailStatus(List<StorageOutDetail> outDetails) {
        Integer ticketStatus = 2;
        String ticketCode = outDetails.get(0).getOutTicketCode();
        String storeCode = outDetails.get(0).getStoreCode();
        for (StorageOutDetail detail : outDetails) {
            Integer returnCount = detail.getReturnCount() + detail.getBackCount();
            detail.setReturnCount(returnCount);
            if (returnCount == null || returnCount <= 0) {
                detail.setStatus(0);
            } else if (detail.getOutCount() <= returnCount) {
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
