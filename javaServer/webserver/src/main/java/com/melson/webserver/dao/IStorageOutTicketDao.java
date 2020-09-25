package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageOutTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/18
 */
@Repository
public interface IStorageOutTicketDao extends JpaRepository<StorageOutTicket,String> {
}
