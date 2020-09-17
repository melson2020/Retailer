package com.melson.webserver.dao;

import com.melson.webserver.entity.StorageInDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
@Repository
public interface IStorageInDetailDao extends JpaRepository<StorageInDetail,String> {
    List<StorageInDetail> findByStorageInTicketCode(String code);
}
