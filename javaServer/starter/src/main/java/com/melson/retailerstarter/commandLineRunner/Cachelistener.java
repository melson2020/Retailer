package com.melson.retailerstarter.commandLineRunner;


import com.melson.base.cache.CacheKey;
import com.melson.base.cache.CacheUtil;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.service.IStoreEmployee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Nelson on 2019/10/24.
 * 项目启动后执行的代码，可以完成一些自定义设置，目前为加载缓存
 * Spring Beans都初始化之后SpringApplication.run()之前执行
 *
 */
@Component
@Order(value=1)
public class Cachelistener implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Cachelistener.class);
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private IStoreEmployee employeeService;

    @Override
    public void run(String... args) throws Exception {
         log.info("--------------------- init system cache -------------------------");
         List<StoreEmployee> employeeList=employeeService.findAll();
         Map<String,StoreEmployee> employeeMap=new HashMap<>(employeeList.size());
         for (StoreEmployee employee:employeeList){
             employeeMap.put(employee.getUserId(),employee);
         }
         cacheUtil.Put(CacheKey.StoreEmployee,employeeMap);
         log.info("---------------------cache init completed ------------------------");

    }
}
