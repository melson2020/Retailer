package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageCountTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/14
 */
@Repository
public interface IStorageCountTicketDao extends JpaRepository<StorageCountTicket,String> {
    StorageCountTicket findByCode(String code);
}
