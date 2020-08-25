package com.melson.webserver.dao;

import com.melson.webserver.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/25
 */
@Repository
public interface IProductDao extends JpaRepository<Product,String> {
}
