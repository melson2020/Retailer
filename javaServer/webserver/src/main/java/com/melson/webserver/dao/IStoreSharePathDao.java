package com.melson.webserver.dao;

import com.melson.webserver.entity.StoreSharePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2021/1/6
 */
@Repository
public interface IStoreSharePathDao extends JpaRepository<StoreSharePath,String> {
    StoreSharePath findByStoreCode(String storeCode);

    StoreSharePath findByShareCode(String shareCode);
}
