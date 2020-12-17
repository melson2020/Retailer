package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.Result;
import com.melson.webserver.dao.ICustomerDao;
import com.melson.webserver.entity.Customer;
import com.melson.webserver.entity.Supply;
import com.melson.webserver.service.ICustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Messi on 2020/12/12
 */
@Service
public class ICustomerImpl extends AbstractService<Customer> implements ICustomer {
    private final ICustomerDao customerDao;

    public ICustomerImpl(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public JpaRepository<Customer, String> getRepository() {
        return customerDao;
    }

    @Override
    public List<Customer> findCustomerByStoreCode(String storeCode) {
        List<Customer> customers=customerDao.findAllByStoreCode(storeCode);
        return customers;
    }

    @Override
    public Customer SaveCustomer(Customer customer) {
        Customer saved=customerDao.save(customer);
        return saved;
    }

    @Override
    public Customer Query(Customer customer) {
        Customer query=customerDao.findById(customer.getId());
        return query;
    }

    @Override
    public Result SaveAndUpdate(Customer customer) {
        Result result = new Result();
        //判断是否存在相同名称
        Customer checkExist=CheckExisting(customer.getName(),customer.getStoreCode());
        if(checkExist!=null){
            if(customer.getId()==checkExist.getId()){
                Customer saved=customerDao.save(customer);
                if(saved==null){
                    result.setResultStatus(-1);
                    result.setMessage("保存失败！");
                }else {
                    result.setData(saved);
                }
            }
            else
            {
                result.setResultStatus(-1);
                result.setMessage("已经存在此客户名称！");
            }
        }
        else
        {
            Customer saved=customerDao.save(customer);
            if(saved==null){
                result.setResultStatus(-1);
                result.setMessage("保存失败！");
            }else {
                result.setData(saved);
            }
        }
        return result;
    }

    private Customer CheckExisting(String name, String storeCode) {
        Customer customer=customerDao.findByNameAndStoreCode(name,storeCode);
        return customer;
    }

}
