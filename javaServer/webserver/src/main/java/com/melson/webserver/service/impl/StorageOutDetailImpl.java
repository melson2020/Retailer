package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IStorageOutDetailDao;
import com.melson.webserver.entity.StorageOutDetail;
import com.melson.webserver.service.IStorageOutDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/25
 */
@Service
public class StorageOutDetailImpl extends AbstractService<StorageOutDetail> implements IStorageOutDetail {
    private final IStorageOutDetailDao storageOutDetailDao;

    public StorageOutDetailImpl(IStorageOutDetailDao storageOutDetailDao) {
        this.storageOutDetailDao = storageOutDetailDao;
    }
    @Override
    public JpaRepository<StorageOutDetail, String> getRepository() {
        return storageOutDetailDao;
    }
}
