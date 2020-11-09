package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.utils.EntityManagerExcuteRs;
import com.melson.base.utils.EntityManagerUtil;
import com.melson.webserver.Vo.StoreInTicketVo;
import com.melson.webserver.dao.IProductBatchDao;
import com.melson.webserver.dao.IStorageInDetailDao;
import com.melson.webserver.dao.IStorageInTicketDao;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.StorageInDetail;
import com.melson.webserver.entity.StorageInTicket;
import com.melson.webserver.service.IStorageInTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
@Service
public class StorageInTicketImpl extends AbstractService<StorageInTicket> implements IStorageInTicket {
    private final IStorageInTicketDao storageInTicketDao;
    private final IStorageInDetailDao storageInDetailDao;
    private final EntityManagerUtil entityManagerUtil;
    private final IProductBatchDao batchDao;

    public StorageInTicketImpl(IStorageInTicketDao storageInTicketDao, IStorageInDetailDao storageInDetailDao, EntityManagerUtil entityManagerUtil, IProductBatchDao batchDao) {
        this.storageInTicketDao = storageInTicketDao;
        this.storageInDetailDao = storageInDetailDao;
        this.entityManagerUtil = entityManagerUtil;
        this.batchDao = batchDao;
    }

    @Override
    public JpaRepository<StorageInTicket, String> getRepository() {
        return storageInTicketDao;
    }

    @Override
    @Transactional
    //1 为成功
    // 其他为执行更新库存失败
    // -1 为保存记录失败
    public Integer SaveStorageInTicket(StorageInTicket ticket) {
        List<StorageInDetail> products = ticket.getProducts();
        List<ProductBatch> batchList = new ArrayList<>(products.size());
        Map<Integer, Integer> sqlMap = new HashMap<>();
        ticket.setCreateTime(new Date());
        for (StorageInDetail detail : products) {
            detail.setStorageInTicketCode(ticket.getCode());
            detail.setStoreCode(ticket.getStoreCode());
            ProductBatch batch = GenerateBatch(detail, ticket);
            batchList.add(batch);
            Integer addCount = sqlMap.get(detail.getProductId());
            if (addCount == null) {
                sqlMap.put(detail.getProductId(), detail.getCount());
            } else {
                addCount += detail.getCount();
                sqlMap.put(detail.getProductId(),addCount);
            }
        }
        List<String> sqls = new ArrayList<>(sqlMap.size());
        for (Integer id : sqlMap.keySet()) {
            String sql = "update product_storage ps set ps.count=(ps.count+" + sqlMap.get(id) + ") WHERE productId=" + id + ";";
            sqls.add(sql);
        }
        List<ProductBatch> productBatchList = batchDao.saveAll(batchList);
        StorageInTicket resTicket = storageInTicketDao.save(ticket);
        List<StorageInDetail> details = storageInDetailDao.saveAll(products);
        if (productBatchList.size() > 0 & resTicket != null & details.size() > 0) {
            EntityManagerExcuteRs rs = entityManagerUtil.BatchUpdateSql(sqls);
            return rs.getStatus();
        } else {
            return -1;
        }
    }

    @Override
    public List<StoreInTicketVo> FindTickets(String startDate, String endDate, String storeCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateBegin = sdf.parse(startDate);
            Date dateEnd = sdf.parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateEnd);
            calendar.add(Calendar.DATE, 1);
            Date newEnd = calendar.getTime();
            List<StorageInTicket> tickets= storageInTicketDao.findByCreateTimeBetweenAndStoreCode(dateBegin, newEnd, storeCode);
            return ConvertToVos(tickets);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<StorageInDetail> FindTicketDetails(String code) {
        return storageInDetailDao.findByStorageInTicketCode(code);
    }

    private ProductBatch GenerateBatch(StorageInDetail detail, StorageInTicket ticket) {
        ProductBatch pb = new ProductBatch();
        pb.setStoreCode(ticket.getStoreCode());
        pb.setProductId(detail.getProductId());
        pb.setBatchNo(ticket.getBatchNo());
        pb.setSupplyId(detail.getSupplyId());
        pb.setSupplyName(detail.getSupplyName());
        pb.setPrice(detail.getPrice());
        pb.setVat(detail.getVat());
        pb.setFinished(0);
        pb.setCount(detail.getCount());
        pb.setCountUnit(detail.getCountUnit());
        pb.setStorageInCode(ticket.getCode());
        pb.setTaxRate(detail.getTaxRate());
        pb.setDiscount(detail.getDiscount());
        pb.setTotalPrice(detail.getTotalPrice());
        pb.setProductName(detail.getProductName());
        //设置批次类型
        pb.setBatchType("IN");
        return pb;
    }

    private List<StoreInTicketVo> ConvertToVos(List<StorageInTicket> tickets){
        Map<String,List<StorageInTicket>> ticketMap=new HashMap<>();
        for (StorageInTicket ticket:tickets){
            List<StorageInTicket> existTickets=ticketMap.get(ticket.getDate());
            if(existTickets==null){
                existTickets=new ArrayList<>();
                existTickets.add(ticket);
                ticketMap.put(ticket.getDate(),existTickets);
            }else {
                existTickets.add(ticket);
            }
        }
        List<StoreInTicketVo> vos=new ArrayList<>(ticketMap.size());
        for (String date:ticketMap.keySet()){
            StoreInTicketVo vo=new StoreInTicketVo();
            vo.setDate(date);
            vo.setTickets(ticketMap.get(date));
            vos.add(vo);
        }
        return vos;
    }
}
