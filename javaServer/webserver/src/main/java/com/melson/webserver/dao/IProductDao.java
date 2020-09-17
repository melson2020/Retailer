package com.melson.webserver.dao;

import com.melson.webserver.dto.ProductDto;
import com.melson.webserver.entity.Product;
import com.melson.webserver.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/25
 */
@Repository
public interface IProductDao extends JpaRepository<Product,String> {
     @Query(value = "SELECT count(id) from product p WHERE storeCode=?1",nativeQuery = true)
     Integer GetCountWithStore(String storeCode);

     List<Product> findByStoreCode(String storeCode);

    @Modifying
    @Query(value = "DELETE from product where id=?1",nativeQuery = true)
    Integer deleteByProductDtoId(Integer id);

    Product findById(Integer id);
}
