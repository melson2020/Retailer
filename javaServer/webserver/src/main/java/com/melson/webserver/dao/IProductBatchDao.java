package com.melson.webserver.dao;

import com.melson.webserver.entity.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@Repository
public interface IProductBatchDao extends JpaRepository<ProductBatch,String> {
    List<ProductBatch> findByStoreCodeAndProductIdAndFinished(String storeCode,Integer productId,Integer finished);
}
