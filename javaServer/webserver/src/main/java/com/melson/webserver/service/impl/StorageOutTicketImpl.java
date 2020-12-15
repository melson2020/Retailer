package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.Vo.OutBoundVo;
import com.melson.webserver.Vo.StorageOutRecordVo;
import com.melson.webserver.Vo.StorageOutTicketDetailVo;
import com.melson.webserver.Vo.StorageOutTicketVo;
import com.melson.webserver.dao.*;
import com.melson.webserver.entity.*;
import com.melson.webserver.service.IStorageOutTicket;
import com.mysql.cj.util.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/18
 */
@Service
public class StorageOutTicketImpl extends AbstractService<StorageOutTicket> implements IStorageOutTicket {
    private final IStorageOutTicketDao storageOutTicketDao;
    private final IStorageOutDetailDao storageOutDetailDao;
    private final IStorageOutBillDao storageOutBillDao;
    private final IStorageOutBillDetailDao storageOutBillDetailDao;
    private final IProductBatchDao productBatchDao;
    private final IProductStorageDao productStorageDao;

    public StorageOutTicketImpl(IStorageOutTicketDao storageOutTicketDao, IStorageOutDetailDao storageOutDetailDao, IStorageOutBillDao storageOutBillDao, IStorageOutBillDetailDao storageOutBillDetailDao, IProductBatchDao productBatchDao, IProductStorageDao productStorageDao) {
        this.storageOutTicketDao = storageOutTicketDao;
        this.storageOutDetailDao = storageOutDetailDao;
        this.storageOutBillDao = storageOutBillDao;
        this.storageOutBillDetailDao = storageOutBillDetailDao;
        this.productBatchDao = productBatchDao;
        this.productStorageDao = productStorageDao;
    }

    @Override
    public JpaRepository<StorageOutTicket, String> getRepository() {
        return storageOutTicketDao;
    }

    @Override
    @Transactional
    public boolean SaveStorageOutTiket(StorageOutTicket ticket, List<StorageOutBillDetail> billList) {
        Date createDate = new Date();
        StorageOutBill bill = GenrateOutBill(billList, ticket, createDate);
        ticket.setCreateTime(createDate);
        ticket.setCategroyCount(ticket.getDetails().size());
        ticket.setBillCode(bill.getCode());
        for (StorageOutDetail d : ticket.getDetails()) {
            d.setOutTicketCode(ticket.getCode());
            d.setStoreCode(ticket.getStoreCode());
            d.setCustomerId(ticket.getCustomerId());
            d.setCustomerName(ticket.getCustomerName());
        }
        //添加出库以及出售单记录
        StorageOutTicket saveTicket = storageOutTicketDao.save(ticket);
        StorageOutBill saveBill = storageOutBillDao.save(bill);
        List<StorageOutBillDetail> billDetails = storageOutBillDetailDao.saveAll(billList);
        //更新库存
        boolean success = UpdateStorage(ticket.getDetails());
        return saveTicket != null & saveBill != null & billDetails != null & success;
    }


    //更新总库存和批次库存
    private boolean UpdateStorage(List<StorageOutDetail> outDetails) {
        Map<String, Integer> batchMap = new HashMap<>();
        Set<Integer> productIds = new HashSet<>();
        Set<String> batchNos = new HashSet<>();
        Map<String, StorageOutDetail> detailMap = new HashMap<>(outDetails.size());
        for (StorageOutDetail detail : outDetails) {
            batchNos.add(detail.getStorageInBatchNo());
            String key = detail.getStorageInBatchNo() + detail.getSupplyId() + detail.getProductId() + detail.getStoreCode();
            Integer batchCount = batchMap.get(key);
            if (batchCount == null) {
                batchMap.put(key, detail.getOutCount());
            } else {
                batchCount += detail.getOutCount();
                batchMap.put(key, batchCount);
            }
            detailMap.put(key, detail);
            productIds.add(detail.getProductId());
        }
        List<ProductBatch> productBatchList = productBatchDao.findByBatchNoIn(batchNos);
        List<ProductStorage> productStorageList = productStorageDao.findByProductIdIn(productIds);
        Map<Integer, ProductStorage> storageMap = new HashMap<>(productStorageList.size());
        //获取待更新库存，以便使用
        for (ProductStorage storage : productStorageList) {
            storageMap.put(storage.getProductId(), storage);
        }
        //更新批次库存,同时刷新库存数据
        for (ProductBatch batch : productBatchList) {
            String key=batch.getBatchNo() + batch.getSupplyId() + batch.getProductId() + batch.getStoreCode();
            Integer mins = batchMap.get(key);
            if (mins != null) {
                batch.setCount(batch.getCount() - mins);
                if (batch.getCount() <= 0) {
                    batch.setFinished(1);
                }
                //库存减少
                ProductStorage storage = storageMap.get(batch.getProductId());
                if (storage != null) {
                    //设置更新前数据记录
                    StorageOutDetail detail=detailMap.get(key);
                    if(detail!=null){
                        detail.setBeforeOutCount(storage.getCount());
                        detail.setAfterOutCount(storage.getCount()-detail.getOutCount());
                    }
                    storage.setCount(storage.getCount() - mins);
                }
            }
        }
        List<ProductBatch> savedBatchs = productBatchDao.saveAll(productBatchList);
        List<ProductStorage> savedStorages = productStorageDao.saveAll(storageMap.values());
        List<StorageOutDetail> saveDetailList = storageOutDetailDao.saveAll(outDetails);
        return savedBatchs != null && savedStorages != null&&saveDetailList!=null;
    }

    private StorageOutBill GenrateOutBill(List<StorageOutBillDetail> billDetails, StorageOutTicket ticket, Date createDate) {
        String billCode = UUID.randomUUID().toString();
        for (StorageOutBillDetail detail : billDetails) {
            detail.setOutBillCode(billCode);
            detail.setCustomerId(ticket.getCustomerId());
            detail.setCustomerName(ticket.getCustomerName());
        }
        StorageOutBill bill = new StorageOutBill();
        bill.setCode(billCode);
        bill.setEmployeeId(ticket.getEmployeeId());
        bill.setEmployeeName(ticket.getEmployeeName());
        bill.setStoreCode(ticket.getStoreCode());
        bill.setCreateTime(createDate);
        bill.setDate(ticket.getDate());
        bill.setCustomerName(ticket.getCustomerName());
        bill.setCustomerId(ticket.getCustomerId());
        SetBillDetails(billDetails, bill);
        return bill;
    }

    private void SetBillDetails(List<StorageOutBillDetail> billDetails, StorageOutBill bill) {
        BigDecimal totalPriceIn = new BigDecimal(0);
        BigDecimal cost = new BigDecimal(0);
        BigDecimal totalPriceOut = new BigDecimal(0);
        BigDecimal sales = new BigDecimal(0);
        BigDecimal profit = new BigDecimal(0);
        for (StorageOutBillDetail detail : billDetails) {
            BigDecimal count = new BigDecimal(detail.getOutCount());
            totalPriceIn = totalPriceIn.add(detail.getUnitPriceIn().multiply(count));
            BigDecimal discountRate;
            if (detail.getDiscount() != null) {
                discountRate = (new BigDecimal(100).subtract(new BigDecimal(detail.getDiscount()))).divide(new BigDecimal(100));
            } else {
                discountRate = (new BigDecimal(100).subtract(new BigDecimal(0))).divide(new BigDecimal(100));
            }
            BigDecimal singleCost = detail.getUnitPriceIn().multiply(discountRate);
            cost = cost.add(singleCost.multiply(count));
            totalPriceOut = totalPriceOut.add(detail.getUnitPriceOut().multiply(count));
            BigDecimal taxRateValue;
            if (!StringUtils.isNullOrEmpty(detail.getTaxRateOut())) {
                taxRateValue = (new BigDecimal(100).subtract(new BigDecimal(detail.getTaxRateOut()))).divide(new BigDecimal(100));
            } else {
                taxRateValue = (new BigDecimal(100).subtract(new BigDecimal(0))).divide(new BigDecimal(100));
            }
            BigDecimal singleSale = detail.getUnitPriceOut().multiply(taxRateValue);
            sales = sales.add(count.multiply(singleSale));
            profit = profit.add(detail.getProfit());
        }
        bill.setTotalPriceIn(totalPriceIn);
        bill.setCost(cost);
        bill.setTotalPriceOut(totalPriceOut);
        bill.setSales(sales);
        bill.setProfit(profit);
    }

    public List<StorageOutRecordVo> FindRecordList(String storeCode, String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateBegin = sdf.parse(startDate);
            Date dateEnd = sdf.parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateEnd);
            calendar.add(Calendar.DATE, 1);
            Date newEnd = calendar.getTime();
            List<StorageOutTicket> outTicketList = storageOutTicketDao.findByCreateTimeBetweenAndStoreCode(dateBegin, newEnd, storeCode);
            List<StorageOutBill> outBillList = storageOutBillDao.findByCreateTimeBetweenAndStoreCode(dateBegin, newEnd, storeCode);
            return GenerateVos(outTicketList, outBillList);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<OutBoundVo> FindOutBoundList(String startDate, String endDate, String storeCode, String permission, String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<OutBoundVo> voList;
            Date dateBegin = sdf.parse(startDate);
            Date dateEnd = sdf.parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateEnd);
            calendar.add(Calendar.DATE, 1);
            Date newEnd = calendar.getTime();
            switch (permission) {
                case "1":
                    voList = GenerateDataByUserId(dateBegin, newEnd, userId);
                    return voList;
                case "2":
                    voList = GenerateDataByStoreCode(dateBegin, newEnd, storeCode);
                    return voList;
                case "3":
                    voList = GenerateDataByStoreCode(dateBegin, newEnd, storeCode);
                    return voList;
                default:
                    voList = GenerateDataByUserId(dateBegin, newEnd, userId);
                    return voList;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    private List<OutBoundVo> GenerateDataByStoreCode(Date dateBegin, Date newEnd, String storeCode) {
        List<Object[]> VoObjs = productStorageDao.findVoByStoreCode(dateBegin, newEnd, storeCode);
        List<OutBoundVo> voList = GenerateVoList(VoObjs);
        return voList;
    }

    private List<OutBoundVo> GenerateDataByUserId(Date dateBegin, Date newEnd, String userId) {
        List<Object[]> VoObjs = productStorageDao.findVoByUserId(dateBegin, newEnd, userId);
        List<OutBoundVo> voList = GenerateVoList(VoObjs);
        return voList;
    }

    private List<OutBoundVo> GenerateVoList(List<Object[]> VoObjs) {
        List<OutBoundVo> voList = new ArrayList<>();
        for (Object[] obj : VoObjs) {
            OutBoundVo r = new OutBoundVo();
            r.setDate(obj[0] == null ? null : obj[0].toString());
            r.setOutBoundNo(obj[1] == null ? null : obj[1].toString());
            r.setSalesName(obj[2] == null ? null : obj[2].toString());
            r.setProduct(obj[3] == null ? null : obj[3].toString());
            r.setSupply(obj[4] == null ? null : obj[4].toString());
            r.setBatchNo(obj[5] == null ? null : obj[5].toString());
            r.setPriceIn(obj[6] == null ? new BigDecimal(0) : new BigDecimal(obj[6].toString()));
            r.setPriceOut(obj[7] == null ? new BigDecimal(0) : new BigDecimal(obj[7].toString()));
            r.setOutCount(obj[8] == null ? 0 : Integer.parseInt(obj[8].toString()));
            r.setProfit(obj[9] == null ? new BigDecimal(0) : new BigDecimal(obj[9].toString()));
            r.setCustomerName(obj[10] == null ? null : obj[10].toString());
            voList.add(r);
        }
        return voList;
    }


    @Override
    public StorageOutTicketDetailVo FindRecordDetail(String ticketCode, String billCode,String storeCode) {
        StorageOutTicketDetailVo vo = new StorageOutTicketDetailVo();
        List<StorageOutDetail> ticketDetails = storageOutDetailDao.findByOutTicketCodeAndStoreCode(ticketCode,storeCode);
        List<StorageOutBillDetail> billDetails = storageOutBillDetailDao.findByOutBillCode(billCode);
        vo.setBillDetails(billDetails);
        vo.setTicketDetails(ticketDetails);
        return vo;
    }

    @Override
    public StorageOutTicket GetTicketInfos(String ticketCode, String storeCode) {
        StorageOutTicket ticket=storageOutTicketDao.findByStoreCodeAndCode(storeCode,ticketCode);
        if(ticket==null)return null;
        List<StorageOutDetail> details=storageOutDetailDao.findByOutTicketCodeAndStoreCode(ticketCode,storeCode);
        ticket.setDetails(details);
        return ticket;
    }

    private List<StorageOutRecordVo> GenerateVos(List<StorageOutTicket> outTicketList, List<StorageOutBill> outBillList) {
        Map<String, StorageOutBill> billMap = new HashMap<>(outBillList.size());
        for (StorageOutBill bill : outBillList) {
            billMap.put(bill.getCode(), bill);
        }
        Map<String, List<StorageOutTicketVo>> voMap = new HashMap<>();
        for (StorageOutTicket outTicket : outTicketList) {
            StorageOutBill existBill = billMap.get(outTicket.getBillCode());
            StorageOutTicketVo vo = new StorageOutTicketVo();
            vo.setOutBill(existBill);
            vo.setOutTicket(outTicket);
            List<StorageOutTicketVo> existList = voMap.get(outTicket.getDate());
            if (existList != null) {
                existList.add(vo);
            } else {
                existList = new ArrayList<>();
                existList.add(vo);
                voMap.put(outTicket.getDate(), existList);
            }
        }
        List<StorageOutRecordVo> voList = new ArrayList<>();
        for (String date : voMap.keySet()) {
            StorageOutRecordVo vo = new StorageOutRecordVo();
            vo.setDate(date);
            vo.setOutTickets(voMap.get(date));
            voList.add(vo);
        }
        return voList;
    }
}
