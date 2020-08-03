package com.melson.wechatmini.resource;

import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nelson on 2020/7/20.
 */
@RestController
@RequestMapping("/testmini")
public class test {
    @RequiredPermission(SecurityLevel.Admin)
    @RequestMapping(value = "/hello")
    public String test(){
        return "helloWorld from wechatmini";
    }
}
