package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageOutBillDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/25
 */
public interface IStorageOutBillDetailDao extends JpaRepository<StorageOutBillDetail,String> {
}
