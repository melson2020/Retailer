package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IStorageOutBillDetailDao;
import com.melson.webserver.entity.StorageOutBillDetail;
import com.melson.webserver.service.IStorageOutBillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/25
 */
@Service
public class StorageOutBillDetailImpl extends AbstractService<StorageOutBillDetail> implements IStorageOutBillDetail {
    private final IStorageOutBillDetailDao storageOutBillDetailDao;

    public StorageOutBillDetailImpl(IStorageOutBillDetailDao storageOutBillDetailDao) {
        this.storageOutBillDetailDao = storageOutBillDetailDao;
    }

    @Override
    public JpaRepository<StorageOutBillDetail, String> getRepository() {
        return storageOutBillDetailDao;
    }
}
