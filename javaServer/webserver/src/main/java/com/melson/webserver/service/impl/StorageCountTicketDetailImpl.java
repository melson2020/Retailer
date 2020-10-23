package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IStorageCountTicketDetailDao;
import com.melson.webserver.entity.StorageCountTicketDetail;
import com.melson.webserver.service.IStorageCountTicketDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/23
 */
@Service
public class StorageCountTicketDetailImpl extends AbstractService<StorageCountTicketDetail> implements IStorageCountTicketDetail {
    private final IStorageCountTicketDetailDao storageCountTicketDetailDao;

    public StorageCountTicketDetailImpl(IStorageCountTicketDetailDao storageCountTicketDetailDao) {
        this.storageCountTicketDetailDao = storageCountTicketDetailDao;
    }

    @Override
    public JpaRepository<StorageCountTicketDetail, String> getRepository() {
        return storageCountTicketDetailDao;
    }
}