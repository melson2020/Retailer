package com.melson.webserver.dao;

import com.melson.webserver.entity.ProductStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@Repository
public interface IProductStorageDao extends JpaRepository<ProductStorage,String> {
    @Query(value = "Select count(id) from product_storage where storeCode=?1",nativeQuery = true)
    Integer GetCountWithStore(String storeCode);

    List<ProductStorage> findByStoreCode(String storeCode);
}
