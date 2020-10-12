package com.melson.webserver.Vo;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/29
 */
public class StorageOutRecordVo {
    private String date;
    private List<StorageOutTicketVo> outTickets;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StorageOutTicketVo> getOutTickets() {
        return outTickets;
    }

    public void setOutTickets(List<StorageOutTicketVo> outTickets) {
        this.outTickets = outTickets;
    }
}
