package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.Result;
import com.melson.webserver.entity.GoodsReturnRecord;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/16
 */
public interface IGoodsReturnRecord extends IService<GoodsReturnRecord> {
    Result SaveGoodsReturnRecords(List<GoodsReturnRecord> returnRecords);
}
