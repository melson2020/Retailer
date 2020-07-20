package com.melson.wechatmini.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nelson on 2020/7/20.
 */
@RestController
@RequestMapping("/testmini")
public class test {
    @RequestMapping(value = "/hello")
    public String test(){
        return "helloWorld from wechatmini";
    }
}
