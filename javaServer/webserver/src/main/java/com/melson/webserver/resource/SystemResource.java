package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.entity.Store;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.service.*;
import com.melson.webserver.dto.StoreDto;
import com.melson.webserver.entity.Menu;
import com.melson.webserver.service.ILoginLogs;
import com.melson.webserver.service.IMenu;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/6
 */
@RestController
@RequestMapping(value = "/system")
public class SystemResource extends BaseResource {
    private final IStore storeService;
    private final IStoreEmployee employeeService;
    private final IMenu menuService;
    private final ILoginLogs loginLogsService;

    public SystemResource(IStore storeService, IStoreEmployee employeeService,IMenu menuService,ILoginLogs loginLogsService) {
        this.storeService = storeService;
        this.employeeService = employeeService;
        this.menuService=menuService;
        this.loginLogsService=loginLogsService;
    }

    @RequestMapping(value = "/registerStore", method = RequestMethod.POST)
    public Result RegisterStore(@RequestBody StoreDto storeDto, HttpServletRequest request) {
        Result result = new Result();
        Store store = storeDto.GenerateStore();
        store.setCreateDate(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String ds=df.format(cal.getTime());
        store.setExpireDate(ds);
        boolean successed = storeService.RegisterStore(store, storeDto.getPassword());
        int res = successed ? 1 : 2;
        result.setResultStatus(res);
        System.out.println("POST Rest Call: /public/registerStore ...");
        return result;
    }

    @RequestMapping(value = "/phoneCheck")
    public Result ValidatePhone(HttpServletRequest request) {
        String phoneNum = request.getParameter("phoneNumber");
        boolean exist = storeService.CheckPhone(phoneNum);
        Result result = new Result();
        result.setData(exist);
        System.out.println("Rest Call: /public/phoneCheck ...");
        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result SystemLogin(@RequestBody StoreEmployee employee, HttpServletRequest request) throws ParseException {
        Result result = new Result();
        StoreEmployee exist = employeeService.Login(employee);
        if(exist!=null) {
            Store store=storeService.findByCode(exist.getStoreCode());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(!StringUtils.isEmpty(store.getExpireDate())) {
                Date date=new Date();
                Date date2 = sdf.parse(store.getExpireDate());
                if (date.getTime() > date2.getTime()) {
                    result.setMessage("shop expired，please contact admin！");
                    result.setResultStatus(-1);
                    return result;
                }
            }
            else
            {
                result.setMessage("shop abnormal，please contact admin！");
                result.setResultStatus(-1);
                return result;
            }
            exist.setStore(store);
            List<Menu> menuList = menuService.GetMenuListWithPermission(exist.getPermission());
            exist.setMenuList(menuList);
            loginLogsService.Records(employee);
        }
        result.setData(exist);
        return result;
    }
}
