package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.Vo.*;
import com.melson.webserver.entity.StorageOutBillDetail;
import com.melson.webserver.entity.StorageOutTicket;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/18
 */
public interface IStorageOutTicket extends IService<StorageOutTicket> {
    boolean SaveStorageOutTiket(StorageOutTicket ticket, List<StorageOutBillDetail> billList);
    List<StorageOutRecordVo> FindRecordList(String storeCode, String startDate, String endDate, String searchValue, String isTax);
    StorageOutTicketDetailVo FindRecordDetail(String ticketCode,String billCode,String storeCode);

    /**
     * 查询出库单详细
     * @param ticketCode
     * @param storeCode
     * @return
     */
    StorageOutTicket GetTicketInfos(String ticketCode,String storeCode);
    List<OutBoundVo> FindOutBoundList(String startDate, String endDate, String storeCode, String permission, String userId,String customerId,String productId,String employeeId);
    List<StorageOutTicket> FindTicketsWithCodeOrCustomerNameAndDate(String searchValue,String date,String storeCode);

    StorageOutTicket FindTicketForGoodsReturn(String storeCode,String tiketCode);

    DashBoardVo GenerateDashboard(String storeCode, String startDate, String endDate);

    List<WechatOutBoundVo> findOutBoundListForWechat(String key, String storeCode);
    StorageOutTicket UpdateOutTicket(StorageOutTicket outTicket);
}
