package com.melson.retailerstarter;

import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/7/24
 */
@RestController
@RequestMapping(value = "/test")
public class testResoruce {
    @RequestMapping(value = "/hello")
    public String test(){
        return "helloWorld";
    }
}
