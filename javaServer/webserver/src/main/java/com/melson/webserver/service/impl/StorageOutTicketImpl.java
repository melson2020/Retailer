package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.utils.EntityManagerUtil;
import com.melson.webserver.Vo.*;
import com.melson.webserver.dao.*;
import com.melson.webserver.dto.StorageOutTicketDashboardDto;
import com.melson.webserver.dto.StorageOutTicketInfoDto;
import com.melson.webserver.entity.*;
import com.melson.webserver.service.IStorageOutTicket;
import com.melson.webserver.utils.EntityUtils;
import com.mysql.cj.util.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    private final EntityManagerUtil entityManagerUtil;
    private final IGoodsReturnRecordDao goodsReturnRecordDao;

    public StorageOutTicketImpl(IStorageOutTicketDao storageOutTicketDao, IStorageOutDetailDao storageOutDetailDao, IStorageOutBillDao storageOutBillDao, IStorageOutBillDetailDao storageOutBillDetailDao, IProductBatchDao productBatchDao, IProductStorageDao productStorageDao, EntityManagerUtil entityManagerUtil, IGoodsReturnRecordDao goodsReturnRecordDao) {
        this.storageOutTicketDao = storageOutTicketDao;
        this.storageOutDetailDao = storageOutDetailDao;
        this.storageOutBillDao = storageOutBillDao;
        this.storageOutBillDetailDao = storageOutBillDetailDao;
        this.productBatchDao = productBatchDao;
        this.productStorageDao = productStorageDao;
        this.entityManagerUtil = entityManagerUtil;
        this.goodsReturnRecordDao = goodsReturnRecordDao;
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
        List<ProductBatch> productBatchList = productBatchDao.findByBatchNoInAndStoreCode(batchNos, outDetails.get(0).getStoreCode());
        List<ProductStorage> productStorageList = productStorageDao.findByProductIdIn(productIds);
        Map<Integer, ProductStorage> storageMap = new HashMap<>(productStorageList.size());
        //获取待更新库存，以便使用
        for (ProductStorage storage : productStorageList) {
            storageMap.put(storage.getProductId(), storage);
        }
        //更新批次库存,同时刷新库存数据
        for (ProductBatch batch : productBatchList) {
            String key = batch.getBatchNo() + batch.getSupplyId() + batch.getProductId() + batch.getStoreCode();
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
                    StorageOutDetail detail = detailMap.get(key);
                    if (detail != null) {
                        detail.setBeforeOutCount(storage.getCount());
                        detail.setAfterOutCount(storage.getCount() - detail.getOutCount());
                    }
                    storage.setCount(storage.getCount() - mins);
                }
            }
        }
        List<ProductBatch> savedBatchs = productBatchDao.saveAll(productBatchList);
        List<ProductStorage> savedStorages = productStorageDao.saveAll(storageMap.values());
        List<StorageOutDetail> saveDetailList = storageOutDetailDao.saveAll(outDetails);
        return savedBatchs != null && savedStorages != null && saveDetailList != null;
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
    public List<OutBoundVo> FindOutBoundList(String startDate, String endDate, String storeCode, String permission, String userId, String customerId, String productId, String employeeId) {
        String sql = "select st.date,st.code as outBoundNo,sd.customerName,st.employeeName as salesName,sd.productName as product,sd.supplyName as supply,sd.storageInBatchNo as batchNo,sb.unitPriceIn as priceIn,sb.unitPriceOut as priceOut,sb.outCount,sd.totalPrice, sb.unitProfit, sb.profit,sd.returnCount,0.0 as salesProfit,sb.countUnit " +
                "FROM storage_out_ticket st LEFT JOIN storage_out_detail sd on st.`code`=sd.outTicketCode " +
                "RIGHT JOIN storage_out_bill_detail sb on sd.code=sb.outDetailCode";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateEnd = sdf.parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateEnd);
            calendar.add(Calendar.DATE, 1);
            Date newEnd = calendar.getTime();
            String newDateEnd = sdf.format(newEnd);
            StringBuffer buffer = new StringBuffer(sql);
            buffer.append(" where st.createTime>'" + startDate + "' and st.createTime<'" + newDateEnd + "'");
            buffer.append(" and st.storeCode='" + storeCode + "'");
            if (permission.equals("1")) {
                buffer.append(" and st.employeeId='" + userId + "'");
            } else {
                if (!StringUtils.isNullOrEmpty(employeeId)) {
                    buffer.append(" and st.employeeId='" + employeeId + "'");
                }
            }
            if (!StringUtils.isNullOrEmpty(customerId)) {
                buffer.append(" and st.customerId=" + customerId);
            }
            if (!StringUtils.isNullOrEmpty(productId)) {
                buffer.append(" and sd.productId=" + productId);
            }
            String excuteSql = buffer.toString();
            List<Object[]> list = entityManagerUtil.ExcuteSql(excuteSql);
            List<OutBoundVo> vos = EntityUtils.castEntity(list, OutBoundVo.class, new OutBoundVo());
            //计算销售利润
            for (OutBoundVo vo : vos) {
                if (vo.getReturnCount() > 0) {
                    BigDecimal salesProfit = vo.getProfit().subtract(vo.getUnitProfit().multiply(new BigDecimal(vo.getReturnCount())));
                    vo.setSalesProfit(salesProfit);
                } else {
                    vo.setSalesProfit(vo.getProfit());
                }
            }
            return vos;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<WechatOutBoundVo> findOutBoundListForWechat(String key, String storeCode) {
        String sql="select st.date,sd.customerName,sd.productName as product,sd.supplyName as supply,sd.outPrice as priceOut,sd.outCount as count,sd.totalPrice,sd.countUnit " +
                "FROM storage_out_ticket st " +
                "LEFT JOIN storage_out_detail sd on st.`code`=sd.outTicketCode ";
        StringBuffer buffer = new StringBuffer(sql);
        buffer.append(" where (sd.customerName like '%"+key+"%' or sd.supplyName like '%"+key+"%' or sd.productName like '%"+key+"%')");
        buffer.append(" and st.storeCode='"+storeCode+"'");
        buffer.append(" ORDER BY st.date");
        buffer.append(" limit 0,30");
        String excuteSql=buffer.toString();
        List<Object[]> list = entityManagerUtil.ExcuteSql(excuteSql);
        List<WechatOutBoundVo> vos = EntityUtils.castEntity(list, WechatOutBoundVo.class, new WechatOutBoundVo());
        return vos;
    }

    @Override
    public List<StorageOutTicket> FindTicketsWithCodeOrCustomerNameAndDate(String searchValue, String date, String storeCode) {
        String sql = "SELECT st.id,st.date,st.code,st.customerName,sd.productName,sd.storageInBatchNo as batchNo,sd.supplyName,sd.outCount,sd.countUnit,st.storeCode,st.status FROM `storage_out_ticket` st right JOIN  storage_out_detail sd on st.`code`=sd.outTicketCode where st.storeCode='" + storeCode + "'";
        StringBuffer sBuffer = new StringBuffer(sql);
        if (org.springframework.util.StringUtils.isEmpty(searchValue)) {
            sBuffer.append("and st.date='" + date + "'");
        } else if (org.springframework.util.StringUtils.isEmpty(date)) {
            String likeStr = "%" + searchValue + "%";
            sBuffer.append("and st.code like '" + likeStr + "' or st.customerName like '" + likeStr + "'");
        } else {
            String likeStr = "%" + searchValue + "%";
            sBuffer.append("and st.code like '" + likeStr + "' or st.customerName like '" + likeStr + "' and st.date='" + date + "'");
        }
        List<Object[]> list = entityManagerUtil.ExcuteSql(sBuffer.toString());
        List<StorageOutTicketInfoDto> dtos = EntityUtils.castEntity(list, StorageOutTicketInfoDto.class, new StorageOutTicketInfoDto());
        return GenerateOutTickets(dtos);
    }

    @Override
    public StorageOutTicket FindTicketForGoodsReturn(String storeCode, String tiketCode) {
        StorageOutTicket outTicket = storageOutTicketDao.findByStoreCodeAndCode(storeCode, tiketCode);
        List<StorageOutDetail> details = storageOutDetailDao.findByOutTicketCodeAndStoreCode(tiketCode, storeCode);
        List<GoodsReturnRecord> backList = goodsReturnRecordDao.findByStoreCodeAndOutTicketCode(storeCode, tiketCode);
        if (backList != null && backList.size() > 0) {
            outTicket.setReturnList(backList);
            //重新填充 退货数量和ticket 状态值
//            Map<String,List<GoodsReturnRecord>> recordMap=new HashMap<>(backList.size());
//            //退货记录按照Key 分类，以便填充details 退货数量 以及ticket 退货list
//            for(GoodsReturnRecord record:backList){
//                String key=record.getProductId()+record.getSupplyId()+record.getBatchNo();
//                List<GoodsReturnRecord> existList=recordMap.get(key);
//                if(existList!=null){
//                    existList.add(record);
//                }else {
//                    existList=new ArrayList<>();
//                    existList.add(record);
//                    recordMap.put(key,existList);
//                }
//            }
//            Integer ticketReturnStatus=0;
//            for(StorageOutDetail detail:details){
//                String key=detail.getProductId()+detail.getSupplyId()+detail.getStorageInBatchNo();
//                List<GoodsReturnRecord> returnList=recordMap.get(key);
//                Integer returnCount=0;
//                for(GoodsReturnRecord grr:returnList){
//                    returnCount+=grr.getCount();
//                }
//                detail.setReturnCount(returnCount);
//                Integer detailStatus=returnCount==0?0:returnCount<detail.getOutCount()?1:2;
//                detail.setStatus(detailStatus);
//                if(detailStatus>0){
//                    ticketReturnStatus=ticketReturnStatus==0?detailStatus:ticketReturnStatus==1?1:detailStatus==2?2:1;
//                }
//            }
//            outTicket.setStatus(ticketReturnStatus);
        }
        outTicket.setDetails(details);
        return outTicket;
    }


    @Override
    public DashBoardVo GenerateDashboard(String storeCode, String startDate, String endDate) {
        List<Object[]> list = storageOutTicketDao.findDashboardDetail(startDate, endDate, storeCode);
        List<StorageOutTicketDashboardDto> dtos = EntityUtils.castEntity(list, StorageOutTicketDashboardDto.class, new StorageOutTicketDashboardDto());
        Map<Integer, DashBoardItemVo> pMap = new HashMap<>();
        Map<String, DashBoardItemVo> eMap = new HashMap<>();
        BigDecimal totalPriceAll = new BigDecimal(0);
        for (StorageOutTicketDashboardDto dto : dtos) {
            //所有的销售总额
            totalPriceAll = totalPriceAll.add(dto.getTotalPrice());
            //生成产品占比数据
            DashBoardItemVo pItem = pMap.get(dto.getProductId());
            if (pItem == null) {
                pItem = new DashBoardItemVo();
                pItem.setId(dto.getProductId().toString());
                pItem.setItemName(dto.getProductName());
                pItem.setItemValue(dto.getTotalPrice());
                pMap.put(dto.getProductId(), pItem);
            } else {
                pItem.setItemValue(pItem.getItemValue().add(dto.getTotalPrice()));
            }
            //生成员工占比数据
            DashBoardItemVo eItem = eMap.get(dto.getEmployeeId());
            if (eItem == null) {
                eItem = new DashBoardItemVo();
                eItem.setId(dto.getEmployeeId());
                eItem.setItemName(dto.getEmployeeName());
                eItem.setItemValue(dto.getTotalPrice());
                eMap.put(dto.getEmployeeId(), eItem);
            } else {
                eItem.setItemValue(eItem.getItemValue().add(dto.getTotalPrice()));
            }
        }

        List<DashBoardItemVo> pList = new ArrayList<>(pMap.values());
        pList.sort(new Comparator<DashBoardItemVo>() {
            @Override
            public int compare(DashBoardItemVo o1, DashBoardItemVo o2) {
                return o2.getItemValue().compareTo(o1.getItemValue());
            }
        });
        List<DashBoardItemVo> eList = new ArrayList<>(eMap.values());
        eList.sort(new Comparator<DashBoardItemVo>() {
            @Override
            public int compare(DashBoardItemVo o1, DashBoardItemVo o2) {
                return o2.getItemValue().compareTo(o1.getItemValue());
            }
        });
        DashBoardVo vo = new DashBoardVo();
        DecimalFormat df = new DecimalFormat("0.00%");
        vo.setProductList(ReSizeListToSix(pList,totalPriceAll,df));
        vo.setEmployeeList(ReSizeListToSix(eList,totalPriceAll,df));
        vo.setSortList(pList);
        return vo;
    }

    private List<DashBoardItemVo> ReSizeListToSix(List<DashBoardItemVo> list, BigDecimal totalPriceAll, DecimalFormat sf) {
        List<DashBoardItemVo> reSizeList = new ArrayList<>();
        DashBoardItemVo newVo = new DashBoardItemVo();
        newVo.setItemName("Others");
        newVo.setItemValue(new BigDecimal(0));
        for (int i = 0; i < list.size(); i++) {
            if (i < 5) {
                String percent = sf.format(list.get(i).getItemValue().divide(totalPriceAll,4, BigDecimal.ROUND_HALF_UP));
                list.get(i).setPercent(percent);
                reSizeList.add(list.get(i));
            } else {
                newVo.setItemValue(newVo.getItemValue().add(list.get(i).getItemValue()));
            }
        }
        if (list.size() > 5) {
            String percentOther = sf.format(newVo.getItemValue().divide(totalPriceAll,4, BigDecimal.ROUND_HALF_UP));
            newVo.setPercent(percentOther);
            reSizeList.add(newVo);
        }
        return reSizeList;
    }


    private List<StorageOutTicket> GenerateOutTickets(List<StorageOutTicketInfoDto> dtos) {

        Map<String, StorageOutTicket> outTicketMap = new HashMap<>();
        for (StorageOutTicketInfoDto dto : dtos) {
            StorageOutTicket out = outTicketMap.get(dto.getCode());
            if (out == null) {
                StorageOutTicket ticket = CreateOutTicket(dto);
                outTicketMap.put(dto.getCode(), ticket);
            } else {
                StorageOutDetail detail = CreateDetail(dto);
                out.getDetails().add(detail);
            }
        }
        return new ArrayList<>(outTicketMap.values());
    }

    private StorageOutTicket CreateOutTicket(StorageOutTicketInfoDto dto) {
        StorageOutTicket ticket = new StorageOutTicket();
        ticket.setDate(dto.getDate());
        ticket.setCode(dto.getCode());
        ticket.setId(dto.getId());
        ticket.setStoreCode(dto.getStoreCode());
        ticket.setStatus(dto.getStatus());
        ticket.setCustomerName(dto.getCustomerName());
        List<StorageOutDetail> details = new ArrayList<>();
        StorageOutDetail detail = CreateDetail(dto);
        details.add(detail);
        ticket.setDetails(details);
        return ticket;
    }

    private StorageOutDetail CreateDetail(StorageOutTicketInfoDto dto) {
        StorageOutDetail detail = new StorageOutDetail();
        detail.setStorageInBatchNo(dto.getBatchNo());
        detail.setProductName(dto.getProductName());
        detail.setSupplyName(dto.getSupplyName());
        detail.setOutCount(dto.getOutCount());
        detail.setCountUnit(dto.getCountUnit());
        return detail;
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
    public StorageOutTicketDetailVo FindRecordDetail(String ticketCode, String billCode, String storeCode) {
        StorageOutTicketDetailVo vo = new StorageOutTicketDetailVo();
        List<StorageOutDetail> ticketDetails = storageOutDetailDao.findByOutTicketCodeAndStoreCode(ticketCode, storeCode);
        List<StorageOutBillDetail> billDetails = storageOutBillDetailDao.findByOutBillCode(billCode);
        vo.setBillDetails(billDetails);
        vo.setTicketDetails(ticketDetails);
        return vo;
    }

    @Override
    public StorageOutTicket GetTicketInfos(String ticketCode, String storeCode) {
        StorageOutTicket ticket = storageOutTicketDao.findByStoreCodeAndCode(storeCode, ticketCode);
        if (ticket == null) return null;
        List<StorageOutDetail> details = storageOutDetailDao.findByOutTicketCodeAndStoreCode(ticketCode, storeCode);
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
