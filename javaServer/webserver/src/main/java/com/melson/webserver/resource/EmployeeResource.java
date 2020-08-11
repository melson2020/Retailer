package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.entity.Permission;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.base.service.IPermission;
import com.melson.base.service.IStoreEmployee;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private final IPermission permissionService;

    public EmployeeResource(IStoreEmployee employeeService,IPermission permissionService) {
        this.employeeService = employeeService;
        this.permissionService=permissionService;
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

    @RequestMapping(value = "/createEmployee",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Manager)
    public Result CreateEmployee(@RequestBody StoreEmployee employee){
        Result result=new Result();
        StoreEmployee saved=employeeService.CreateEmployee(employee);
        if(saved==null){
            result.setResultStatus(-1);
            result.setMessage("create fail");
        }else {
            result.setData(saved);
        }
        return result;
    }

    @RequestMapping(value = "/checkLoginName",method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Manager)
    public Result CheckLoginName(HttpServletRequest request){
        String loginName=request.getParameter("loginName");
        if(StringUtils.isEmpty(loginName))GenerateResult(ResultType.ParametersNeeded);
        StoreEmployee exist=employeeService.findByLoginName(loginName);
        Result result=new Result();
        result.setData(exist==null);
        return result;
    }

    @RequestMapping(value = "/permissionList",method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetPermissionList(HttpServletRequest request){
        Result result=new Result();
        List<Permission> permissionList=permissionService.findAll();
        result.setData(permissionList);
        return result;
    }

    @RequestMapping(value = "/updateEmployee",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Manager)
    public Result UpdateEmployee(@RequestBody StoreEmployee storeEmployee,HttpServletRequest request){
         if(StringUtils.isEmpty(storeEmployee.getUserId())|| storeEmployee.getPermission()==null||storeEmployee.getGender()==null|| StringUtils.isEmpty(storeEmployee.getPhone())
                 || StringUtils.isEmpty(storeEmployee.getUserName())){
             return GenerateResult(ResultType.ParametersNeeded);
         }
         Integer savedCount=employeeService.UpdateEmployee(storeEmployee);
         Result result=new Result();
         result.setResultStatus(savedCount>0?1:-1);
         result.setData(savedCount);
         return result;
    }

    @RequestMapping(value = "/deleteEmployee",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Manager)
    public Result DeleteEmployee(@RequestBody StoreEmployee storeEmployee,HttpServletRequest request){
        if(StringUtils.isEmpty(storeEmployee.getUserId())){
            return GenerateResult(ResultType.ParametersNeeded);
        }
        Integer deleteCount=employeeService.DeleteEmployee(storeEmployee);
        Result result=new Result();
        result.setResultStatus(deleteCount>0?1:-1);
        result.setData(deleteCount);
        return result;
    }

}
