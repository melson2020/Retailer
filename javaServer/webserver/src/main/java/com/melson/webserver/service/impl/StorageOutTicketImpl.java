package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IProductBatchDao;
import com.melson.webserver.dao.IStorageOutTicketDao;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.StorageOutTicket;
import com.melson.webserver.service.IStorageOutTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/18
 */
@Service
public class StorageOutTicketImpl extends AbstractService<StorageOutTicket> implements IStorageOutTicket {
    private final IStorageOutTicketDao storageOutTicketDao;
    private final IProductBatchDao batchDao;

    public StorageOutTicketImpl(IStorageOutTicketDao storageOutTicketDao,IProductBatchDao batchDao) {
        this.storageOutTicketDao = storageOutTicketDao;
        this.batchDao=batchDao;
    }
    @Override
    public JpaRepository<StorageOutTicket, String> getRepository() {
        return storageOutTicketDao;
    }
}
