package com.melson.webserver.Vo;

import com.melson.webserver.entity.StorageOutBillDetail;
import com.melson.webserver.entity.StorageOutDetail;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/30
 */
public class StorageOutTicketDetailVo {
    List<StorageOutDetail> ticketDetails;
    List<StorageOutBillDetail> billDetails;

    public List<StorageOutDetail> getTicketDetails() {
        return ticketDetails;
    }

    public void setTicketDetails(List<StorageOutDetail> ticketDetails) {
        this.ticketDetails = ticketDetails;
    }

    public List<StorageOutBillDetail> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<StorageOutBillDetail> billDetails) {
        this.billDetails = billDetails;
    }
}
