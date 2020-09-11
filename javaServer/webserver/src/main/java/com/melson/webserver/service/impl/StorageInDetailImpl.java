package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IStorageInDetailDao;
import com.melson.webserver.entity.StorageInDetail;
import com.melson.webserver.service.IStorageInDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
public class StorageInDetailImpl extends AbstractService<StorageInDetail> implements IStorageInDetail {
    private final IStorageInDetailDao storageInDetailDao;

    public StorageInDetailImpl(IStorageInDetailDao storageInDetailDao) {
        this.storageInDetailDao = storageInDetailDao;
    }

    @Override
    public JpaRepository<StorageInDetail, String> getRepository() {
        return storageInDetailDao;
    }
}
