package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IStorageOutBillDao;
import com.melson.webserver.entity.StorageOutBill;
import com.melson.webserver.service.IStorageOutBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/25
 */
@Service
public class StorageOutBillImpl extends AbstractService<StorageOutBill> implements IStorageOutBill {
    private final IStorageOutBillDao storageOutBillDao;

    public StorageOutBillImpl(IStorageOutBillDao storageOutBillDao) {
        this.storageOutBillDao = storageOutBillDao;
    }

    @Override
    public JpaRepository<StorageOutBill, String> getRepository() {
        return storageOutBillDao;
    }
}
