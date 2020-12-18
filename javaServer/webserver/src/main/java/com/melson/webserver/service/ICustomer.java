package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.Result;
import com.melson.webserver.entity.Customer;
import com.melson.webserver.entity.Supply;

import java.util.List;

/**
 * Created by Messi on 2020/12/12
 */
public interface ICustomer extends IService<Customer> {
    List<Customer> findCustomerByStoreCode(String storeCode);

    Customer SaveCustomer(Customer customer);

    Customer Query(Customer customer);

    Result SaveAndUpdate(Customer customer);
}
