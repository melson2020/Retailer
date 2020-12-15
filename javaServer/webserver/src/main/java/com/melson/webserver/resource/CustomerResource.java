package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.entity.Customer;
import com.melson.webserver.entity.Supply;
import com.melson.webserver.service.ICustomer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Messi on 2020/12/12
 */
@RestController
@RequestMapping("/customer")
public class CustomerResource extends BaseResource {
    private final ICustomer customerService;

    public CustomerResource(ICustomer customer) {
        this.customerService = customer;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetCustomerList(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(storeCode)){
            return this.GenerateResult(ResultType.ParametersNeeded);
        }
        List<Customer> customers = customerService.findCustomerByStoreCode(storeCode);
        Result result=new Result();
        result.setData(customers);
        System.out.println("Rest Call: /customer/list ...");
        return  result;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result CreateCustomer(@RequestBody Customer customer){
        Result result=new Result();
        Customer saved=customerService.SaveCustomer(customer);
        if(saved==null){
            result.setResultStatus(-1);
            result.setMessage("create fail");
        }else {
            result.setData(saved);
        }
        System.out.println("Rest Call: /customer/save ...");
        return result;
    }

    @RequestMapping(value = "/query",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result QueryCustomer(@RequestBody Customer customer){
        Customer cus=customerService.Query(customer);
        Result result=new Result();
        result.setData(cus);
        System.out.println("Rest Call: /customer/query ...");
        return result;
    }

}
