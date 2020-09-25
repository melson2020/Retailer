package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageOutBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/25
 */
@Repository
public interface IStorageOutBillDao extends JpaRepository<StorageOutBill,String> {
}
