package com.melson.webserver.dao;

import com.melson.webserver.entity.SupplyProduct;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Messi on 2020/8/6
 */
public interface ISupplyProductDao extends JpaRepository<SupplyProduct,String> {

}
