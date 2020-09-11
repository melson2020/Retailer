package com.melson.webserver.dao;

import com.melson.webserver.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/4
 */
@Repository
public interface ITaxRateDao extends JpaRepository<TaxRate,String> {
}
