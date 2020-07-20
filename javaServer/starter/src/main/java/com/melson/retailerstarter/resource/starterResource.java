package com.melson.retailerstarter.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nelson on 2020/7/20.
 */
@RestController
@RequestMapping(value = "/test")
public class starterResource {
    @RequestMapping(value = "/hello")
    public String test(){
        return "hello world";
    }
}
