package com.melson.webserver.Vo;

import com.melson.webserver.entity.StorageOutBill;
import com.melson.webserver.entity.StorageOutTicket;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/29
 */
public class StorageOutTicketVo {
    private StorageOutTicket outTicket;
    private StorageOutBill outBill;

    public StorageOutTicket getOutTicket() {
        return outTicket;
    }

    public void setOutTicket(StorageOutTicket outTicket) {
        this.outTicket = outTicket;
    }

    public StorageOutBill getOutBill() {
        return outBill;
    }

    public void setOutBill(StorageOutBill outBill) {
        this.outBill = outBill;
    }
}
