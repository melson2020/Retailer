package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.Result;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.StorageCountTicket;
import com.melson.webserver.entity.StorageCountTicketDetail;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/23
 */
public interface IStorageCountTicketDetail extends IService<StorageCountTicketDetail> {
    Result SaveDetailWithCountedList(List<ProductStorageDto> dtoList, StorageCountTicket ticket);
}
