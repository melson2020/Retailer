package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.entity.Store;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.service.*;
import com.melson.webserver.dto.StoreDto;
import com.melson.webserver.entity.Menu;
import com.melson.webserver.service.IMenu;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    public SystemResource(IStore storeService, IStoreEmployee employeeService,IMenu menuService) {
        this.storeService = storeService;
        this.employeeService = employeeService;
        this.menuService=menuService;
    }

    @RequestMapping(value = "/registerStore", method = RequestMethod.POST)
    public Result RegisterStore(@RequestBody StoreDto storeDto, HttpServletRequest request) {
        Result result = new Result();
        Store store = storeDto.GenerateStore();
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
    public Result SystemLogin(@RequestBody StoreEmployee employee, HttpServletRequest request) {
        Result result = new Result();
        StoreEmployee exist = employeeService.Login(employee);
        if(exist!=null) {
            List<Menu> menuList = menuService.GetMenuListWithPermission(exist.getPermission());
            exist.setMenuList(menuList);
        }
        result.setData(exist);
        return result;
    }
}
