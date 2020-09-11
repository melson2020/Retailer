package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageInTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
@Repository
public interface IStorageInTicketDao extends JpaRepository<StorageInTicket,String> {
}
