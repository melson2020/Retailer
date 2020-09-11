package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.utils.EntityManagerExcuteRs;
import com.melson.webserver.entity.StorageInTicket;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
public interface IStorageInTicket extends IService<StorageInTicket> {
    Integer SaveStorageInTicket(StorageInTicket ticket);
}
