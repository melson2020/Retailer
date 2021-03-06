package com.melson.webserver.resource;

import com.melson.webserver.entity.LoginLogs;
import com.melson.webserver.service.ILoginLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nelson on 2020/7/20.
 */
@RestController
@RequestMapping("/testweb")
public class testResource {
    @Autowired
    private ILoginLogs loginLogsService;

    @RequestMapping(value = "/hello")
    public String test() {
        return "helloWorld from webServer";
    }

    @RequestMapping(value = "/testDemo1")
    public LoginLogs Demo1() {
        String value = "ssss_55\"x7\"";
        return loginLogsService.FindBylogs(value);
    }
}
