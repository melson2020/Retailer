package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.Result;
import com.melson.webserver.dao.IGoodsReturnRecordDao;
import com.melson.webserver.dao.IStorageOutBillDetailDao;
import com.melson.webserver.entity.GoodsReturnRecord;
import com.melson.webserver.entity.StorageOutBillDetail;
import com.melson.webserver.service.IGoodsReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/16
 */
@Service
public class GoodsReturnRecordImpl extends AbstractService<GoodsReturnRecord> implements IGoodsReturnRecord {
    private final IGoodsReturnRecordDao goodsReturnRecordDao;
    private final IStorageOutBillDetailDao billDetailDao;

    public GoodsReturnRecordImpl(IGoodsReturnRecordDao goodsReturnRecordDao, IStorageOutBillDetailDao billDetailDao) {
        this.goodsReturnRecordDao = goodsReturnRecordDao;
        this.billDetailDao = billDetailDao;
    }

    @Override
    public JpaRepository<GoodsReturnRecord, String> getRepository() {
        return goodsReturnRecordDao;
    }

    @Override
    public Result SaveGoodsReturnRecords(List<GoodsReturnRecord> returnRecords) {
        Result result=new Result();
        String billCode=returnRecords.get(0).getBillCode();
        List<StorageOutBillDetail> billDetails=billDetailDao.findByOutBillCode(billCode);
        if(billDetails==null||billDetails.size()<=0){
            result.setResultStatus(-1);
            result.setMessage("can find bill details");
            return result;
        }
        Map<String,StorageOutBillDetail> detailMap=new HashMap<>(billDetails.size());
        for (StorageOutBillDetail detail:billDetails){

        }
        return result;
    }
}
