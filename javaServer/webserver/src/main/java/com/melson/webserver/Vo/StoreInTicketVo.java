package com.melson.webserver.Vo;

import com.melson.webserver.entity.StorageInTicket;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/14
 */
public class StoreInTicketVo implements Comparable<StoreInTicketVo> {
    private String date;
    private List<StorageInTicket> tickets;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StorageInTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<StorageInTicket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public int compareTo(StoreInTicketVo o) {
        return this.date.compareTo(o.getDate())*(-1);
    }
}
