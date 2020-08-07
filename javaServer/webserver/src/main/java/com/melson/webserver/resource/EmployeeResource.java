package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.base.service.IStoreEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/7
 */
@RestController
@RequestMapping(value = "/employee")
public class EmployeeResource extends BaseResource {
    private final IStoreEmployee employeeService;

    public EmployeeResource(IStoreEmployee employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(value = "/employeeList")
    @RequiredPermission(SecurityLevel.Manager)
    public Result GetEmployeeList(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(storeCode)){
           return this.GenerateResult(ResultType.ParametersNeeded);
        }
        List<StoreEmployee> employeeList=employeeService.findByStoreCode(storeCode);
        Result result=new Result();
        result.setData(employeeList);
        return result;
    }
}
