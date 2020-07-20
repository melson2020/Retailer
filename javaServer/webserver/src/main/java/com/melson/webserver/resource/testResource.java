package com.melson.webserver.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nelson on 2020/7/20.
 */
@RestController
@RequestMapping("/testweb")
public class testResource {
    @RequestMapping(value = "/hello")
    public String test(){
        return "helloWorld from webserver";
    }
}
