package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageOutDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/25
 */
@Repository
public interface IStorageOutDetailDao extends JpaRepository<StorageOutDetail,String> {
    List<StorageOutDetail> findByOutTicketCodeAndStoreCode(String ticketCode,String storeCode);
}
