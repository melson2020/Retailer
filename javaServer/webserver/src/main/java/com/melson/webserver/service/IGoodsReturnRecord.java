package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.Result;
import com.melson.webserver.entity.GoodsReturnRecord;
import com.melson.webserver.entity.StorageOutDetail;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/16
 */
public interface IGoodsReturnRecord extends IService<GoodsReturnRecord> {
    Result SaveGoodsReturnRecords(List<GoodsReturnRecord> returnRecords,List<StorageOutDetail> details);
    List<GoodsReturnRecord> FindRecords(String storeCode,String startDate,String endDate,String customerId,String productId,String employeeId);
}
