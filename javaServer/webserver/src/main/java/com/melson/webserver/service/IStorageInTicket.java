package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.utils.EntityManagerExcuteRs;
import com.melson.webserver.Vo.StoreInTicketVo;
import com.melson.webserver.entity.StorageInDetail;
import com.melson.webserver.entity.StorageInTicket;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
public interface IStorageInTicket extends IService<StorageInTicket> {
    Integer SaveStorageInTicket(StorageInTicket ticket);
    List<StoreInTicketVo> FindTickets(String startDate, String endDate, String storeCode);
    List<StorageInDetail> FindTicketDetails(String code);
}
