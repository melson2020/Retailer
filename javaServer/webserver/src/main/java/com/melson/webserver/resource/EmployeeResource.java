package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.cache.CacheKey;
import com.melson.base.cache.CacheUtil;
import com.melson.base.entity.Permission;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.base.service.IPermission;
import com.melson.base.service.IStoreEmployee;
import com.melson.webserver.dto.EmployeeDto;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
    private final CacheUtil cacheUtil;

    public EmployeeResource(IStoreEmployee employeeService,IPermission permissionService, CacheUtil catchUtil) {
        this.employeeService = employeeService;
        this.permissionService=permissionService;
        this.cacheUtil=catchUtil;
    }

    @RequestMapping(value = "/employeeList")
    @RequiredPermission(SecurityLevel.Admin)
    public Result GetEmployeeList(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(storeCode)){
           return this.GenerateResult(ResultType.ParametersNeeded);
        }
        List<StoreEmployee> employeeList=employeeService.findByStoreCode(storeCode);
        Result result=new Result();
        result.setData(employeeList);
        System.out.println("Rest Call: /employee/employeeList ...");
        return result;
    }

    @RequestMapping(value = "/createEmployee",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Admin)
    public Result CreateEmployee(@RequestBody StoreEmployee employee){
        Result result=new Result();
        StoreEmployee saved=employeeService.CreateEmployee(employee);
        if(saved==null){
            result.setResultStatus(-1);
            result.setMessage("create fail");
        }else {
            result.setData(saved);
        }
        Map<String, StoreEmployee> map = cacheUtil.GetObjectValue(CacheKey.StoreEmployee, Map.class);
        map.put(saved.getUserId(),saved);
        System.out.println("Rest Call: /employee/createEmployee ...");
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
        System.out.println("Rest Call: /employee/checkLoginName ...");
        return result;
    }

    @RequestMapping(value = "/permissionList",method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Admin)
    public Result GetPermissionList(HttpServletRequest request){
        Result result=new Result();
        List<Permission> permissionList=permissionService.findAll();
        result.setData(permissionList);
        System.out.println("Rest Call: /employee/permissionList ...");
        return result;
    }

    @RequestMapping(value = "/updateEmployee",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Admin)
    public Result UpdateEmployee(@RequestBody StoreEmployee storeEmployee,HttpServletRequest request){
         if(StringUtils.isEmpty(storeEmployee.getUserId())|| storeEmployee.getPermission()==null||storeEmployee.getGender()==null|| StringUtils.isEmpty(storeEmployee.getPhone())
                 || StringUtils.isEmpty(storeEmployee.getUserName())){
             return GenerateResult(ResultType.ParametersNeeded);
         }
         Integer savedCount=employeeService.UpdateEmployee(storeEmployee);
         Result result=new Result();
         if(savedCount>0){
             result.setResultStatus(1);
             //更新缓存
             Map<String, StoreEmployee> map = cacheUtil.GetObjectValue(CacheKey.StoreEmployee, Map.class);
             StoreEmployee emp=map.get(storeEmployee.getUserId());
             emp.setPermission(storeEmployee.getPermission());
//             map.put(storeEmployee.getUserId(),storeEmployee);
         }
         else
         {
             result.setResultStatus(-1);
         }
//         result.setResultStatus(savedCount>0?1:-1);
         result.setData(savedCount);
         System.out.println("Rest Call: /employee/updateEmployee ...");
         return result;
    }

    @RequestMapping(value = "/deleteEmployee",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Admin)
    public Result DeleteEmployee(@RequestBody StoreEmployee storeEmployee,HttpServletRequest request){
        if(StringUtils.isEmpty(storeEmployee.getUserId())){
            return GenerateResult(ResultType.ParametersNeeded);
        }
        Integer deleteCount=employeeService.DeleteEmployee(storeEmployee);
        Result result=new Result();
        if(deleteCount>0){
            result.setResultStatus(1);
            //更新缓存
            Map<String, StoreEmployee> map = cacheUtil.GetObjectValue(CacheKey.StoreEmployee, Map.class);
            map.remove(storeEmployee.getUserId());
        }
        else {
            result.setResultStatus(-1);
        }
//        result.setResultStatus(deleteCount>0?1:-1);
        result.setData(deleteCount);
        System.out.println("Rest Call: /employee/deleteEmployee ...");
        return result;
    }

    @RequestMapping(value = "/restPassword",method = RequestMethod.POST)
    public Result ResetPass(@RequestBody EmployeeDto employeeDto, HttpServletRequest request){
        if(employeeDto.hasEmptyParams())return this.GenerateResult(ResultType.ParametersNeeded);
        Result result=new Result();
        Integer reseted=employeeService.RetSetPassword(employeeDto.getUserId(),employeeDto.getLoginName(),employeeDto.getOldPass(),employeeDto.getNewPass());
        if(reseted==0){
            result.setResultStatus(-1);
            result.setMessage("reset count: 0");
        }
        System.out.println("Rest Call: /employee/restPassword ...");
        return result;
    }

}
