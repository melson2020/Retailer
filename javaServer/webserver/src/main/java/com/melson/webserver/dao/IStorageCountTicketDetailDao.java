package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageCountTicketDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/23
 */
@Repository
public interface IStorageCountTicketDetailDao extends JpaRepository<StorageCountTicketDetail,String> {
    List<StorageCountTicketDetail> findByTicketCode(String ticketCode);
    List<StorageCountTicketDetail> findByStoreCodeAndSupplyNameIsNull(String storeCode);
}
