package com.melson.webserver.dto;

import com.melson.webserver.entity.StorageOutBillDetail;
import com.melson.webserver.entity.StorageOutTicket;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/28
 */
public class StorageOutTiketDto {
    private StorageOutTicket outTicket;
    private List<StorageOutBillDetail> billDetailList;

    public StorageOutTicket getOutTicket() {
        return outTicket;
    }

    public void setOutTicket(StorageOutTicket outTicket) {
        this.outTicket = outTicket;
    }

    public List<StorageOutBillDetail> getBillDetailList() {
        return billDetailList;
    }

    public void setBillDetailList(List<StorageOutBillDetail> billDetailList) {
        this.billDetailList = billDetailList;
    }
}
