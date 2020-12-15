package com.melson.webserver.dao;

import com.melson.webserver.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Messi on 2020/12/12
 */
public interface ICustomerDao extends JpaRepository<Customer,String> {


    List<Customer> findAllByStoreCode(String storeCode);

    Customer findById(Integer id);
}
