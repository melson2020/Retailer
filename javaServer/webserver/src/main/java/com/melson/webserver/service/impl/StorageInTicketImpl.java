package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.utils.EntityManagerExcuteRs;
import com.melson.base.utils.EntityManagerUtil;
import com.melson.webserver.Vo.StoreInTicketVo;
import com.melson.webserver.dao.IProductBatchDao;
import com.melson.webserver.dao.IProductStorageDao;
import com.melson.webserver.dao.IStorageInDetailDao;
import com.melson.webserver.dao.IStorageInTicketDao;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;
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
    private final IProductStorageDao storageDao;
    private final IStorageInDetailDao storageInDetailDao;
    private final EntityManagerUtil entityManagerUtil;
    private final IProductBatchDao batchDao;

    public StorageInTicketImpl(IStorageInTicketDao storageInTicketDao, IProductStorageDao storageDao, IStorageInDetailDao storageInDetailDao, EntityManagerUtil entityManagerUtil, IProductBatchDao batchDao) {
        this.storageInTicketDao = storageInTicketDao;
        this.storageDao = storageDao;
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
        //用于存储productId 和变化总数
        Map<Integer, Integer> sqlMap = new HashMap<>();
        List<ProductStorage> productStorageList=storageDao.findByStoreCode(ticket.getStoreCode());
        if(productStorageList.size()<=0)return -1;
        //拿出库存备用，以便后续找出需要更新的库存
        Map<Integer,ProductStorage> storageMap=new HashMap<>(productStorageList.size());
        for(ProductStorage storage:productStorageList){
            storageMap.put(storage.getProductId(),storage);
        }
        ticket.setCreateTime(new Date());
        for (StorageInDetail detail : products) {
            detail.setStorageInTicketCode(ticket.getCode());
            detail.setStoreCode(ticket.getStoreCode());
            detail.setBatchNo(ticket.getBatchNo());
            ProductStorage storage=storageMap.get(detail.getProductId());
            ProductBatch batch = GenerateBatch(detail, ticket);
            batchList.add(batch);
            Integer addCount = sqlMap.get(detail.getProductId());
            //记录变化之前的商品总数
            if(storage!=null){
                detail.setBeforeStorageCount(storage.getCount()+(addCount==null?0:addCount));
            }
            if (addCount == null) {
                sqlMap.put(detail.getProductId(), detail.getCount());
            } else {
                addCount += detail.getCount();
                sqlMap.put(detail.getProductId(),addCount);
            }

            detail.setAfterInCount(sqlMap.get(detail.getProductId())+storage.getCount());
        }

       List<ProductStorage> updateStorageList=new ArrayList<>();
        for (Integer id : sqlMap.keySet()) {
           ProductStorage storage=storageMap.get(id);
           if(storage!=null){
               storage.setCount(storage.getCount()+sqlMap.get(id));
               updateStorageList.add(storage);
           }
        }
        List<ProductBatch> productBatchList = batchDao.saveAll(batchList);
        StorageInTicket resTicket = storageInTicketDao.save(ticket);
        List<StorageInDetail> details = storageInDetailDao.saveAll(products);
        List<ProductStorage> updated=storageDao.saveAll(updateStorageList);
        if (productBatchList.size() > 0 & resTicket != null & details.size() > 0&updated.size()>0) {
            return 1;
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
