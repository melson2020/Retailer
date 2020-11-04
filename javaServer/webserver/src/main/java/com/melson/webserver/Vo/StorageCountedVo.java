package com.melson.webserver.Vo;

import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.StorageCountTicket;

import java.util.List;

/**
 * @Author Nelson
 * @Description 盘点表更新数据载体
 * @Date 2020/10/28
 */
public class StorageCountedVo {
    private StorageCountTicket ticket;
    private List<ProductStorageDto> dtoList;

    public StorageCountTicket getTicket() {
        return ticket;
    }

    public void setTicket(StorageCountTicket ticket) {
        this.ticket = ticket;
    }

    public List<ProductStorageDto> getDtoList() {
        return dtoList;
    }

    public void setDtoList(List<ProductStorageDto> dtoList) {
        this.dtoList = dtoList;
    }
}
