package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
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
        }
        //添加出库以及出售单记录
        StorageOutTicket saveTicket = storageOutTicketDao.save(ticket);
        List<StorageOutDetail> saveDetailList = storageOutDetailDao.saveAll(ticket.getDetails());
        StorageOutBill saveBill = storageOutBillDao.save(bill);
        List<StorageOutBillDetail> billDetails = storageOutBillDetailDao.saveAll(billList);
        //更新库存
        boolean success = UpdateStorage(saveDetailList);
        return saveTicket != null & saveDetailList != null & saveBill != null & billDetails != null & success;
    }

    //更新总库存和批次库存
    private boolean UpdateStorage(List<StorageOutDetail> outDetails) {
        Map<String, Integer> batchMap = new HashMap<>();
        Map<Integer, Integer> productMap = new HashMap<>();
        for (StorageOutDetail detail : outDetails) {
            Integer batchCount = batchMap.get(detail.getStorageInBatchNo());
            if (batchCount == null) {
                batchMap.put(detail.getStorageInBatchNo(), detail.getOutCount());
            } else {
                batchCount += detail.getOutCount();
            }
            Integer productCount = productMap.get(detail.getProductId());
            if (productCount == null) {
                productMap.put(detail.getProductId(), detail.getOutCount());
            } else {
                productCount += detail.getOutCount();
                productMap.put(detail.getProductId(), productCount);
            }
        }
        List<ProductBatch> productBatchList = productBatchDao.findByBatchNoIn(batchMap.keySet());
        List<ProductStorage> productStorageList = productStorageDao.findByProductIdIn(productMap.keySet());
        for (ProductBatch batch : productBatchList) {
            Integer mins = batchMap.get(batch.getBatchNo());
            if (mins != null) {
                batch.setCount(batch.getCount() - mins);
                if (batch.getCount() <= 0) {
                    batch.setFinished(1);
                }
            }
        }
        for (ProductStorage storage : productStorageList) {
            Integer mins = productMap.get(storage.getProductId());
            if (mins != null) {
                storage.setCount(storage.getCount() - mins);
            }
        }
        List<ProductBatch> savedBatchs = productBatchDao.saveAll(productBatchList);
        List<ProductStorage> savedStorages = productStorageDao.saveAll(productStorageList);
        return savedBatchs != null && savedStorages != null;
    }

    private StorageOutBill GenrateOutBill(List<StorageOutBillDetail> billDetails, StorageOutTicket ticket, Date createDate) {
        String billCode = UUID.randomUUID().toString();
        for (StorageOutBillDetail detail : billDetails) {
            detail.setOutBillCode(billCode);
        }
        StorageOutBill bill = new StorageOutBill();
        bill.setCode(billCode);
        bill.setEmployeeId(ticket.getEmployeeId());
        bill.setEmployeeName(ticket.getEmployeeName());
        bill.setStoreCode(ticket.getStoreCode());
        bill.setCreateTime(createDate);
        bill.setDate(ticket.getDate());
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
            if(detail.getDiscount()!=null) {
                discountRate = (new BigDecimal(100).subtract(new BigDecimal(detail.getDiscount()))).divide(new BigDecimal(100));
            }
            else
            {
                discountRate=(new BigDecimal(100).subtract(new BigDecimal(0))).divide(new BigDecimal(100));
            }
            BigDecimal singleCost = detail.getUnitPriceIn().multiply(discountRate);
            cost = cost.add(singleCost.multiply(count));
            totalPriceOut = totalPriceOut.add(detail.getUnitPriceOut().multiply(count));
            BigDecimal taxRateValue;
            if(!StringUtils.isNullOrEmpty(detail.getTaxRateOut())) {
                taxRateValue = (new BigDecimal(100).subtract(new BigDecimal(detail.getTaxRateOut()))).divide(new BigDecimal(100));
            }
            else
            {
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
    public StorageOutTicketDetailVo FindRecordDetail(String ticketCode, String billCode) {
       StorageOutTicketDetailVo vo=new StorageOutTicketDetailVo();
       List<StorageOutDetail> ticketDetails=storageOutDetailDao.findByOutTicketCode(ticketCode);
       List<StorageOutBillDetail> billDetails=storageOutBillDetailDao.findByOutBillCode(billCode);
       vo.setBillDetails(billDetails);
       vo.setTicketDetails(ticketDetails);
       return vo;
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
