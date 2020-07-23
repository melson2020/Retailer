package com.melson.retailerstarter.resource;


import com.melson.base.entity.City;
import com.melson.base.service.ICity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Nelson on 2020/7/20.
 */
@RestController
@RequestMapping(value = "/test")
public class starterResource {
    private final ICity cityService;

    public starterResource(ICity cityService) {
        this.cityService = cityService;
    }

    @RequestMapping(value = "/hello")
    public String test(){
//        List<EmployeePermission> employeePermission=employeePermissionDao.findAll();
        return "hello world";
    }
    @RequestMapping(value = "/city")
    public List<City> GetCityList(){
        return cityService.findAll();
    }
}
