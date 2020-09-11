package com.melson.retailerstarter.commandLineRunner;


import com.melson.base.cache.CacheKey;
import com.melson.base.cache.CacheUtil;
import com.melson.base.entity.Area;
import com.melson.base.entity.City;
import com.melson.base.entity.Province;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.service.IArea;
import com.melson.base.service.ICity;
import com.melson.base.service.IProvince;
import com.melson.base.service.IStoreEmployee;
import com.melson.webserver.entity.SysConfig;
import com.melson.webserver.service.ISysConfig;
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
 */
@Component
@Order(value = 1)
public class Cachelistener implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Cachelistener.class);
    private final CacheUtil cacheUtil;
    private final IStoreEmployee employeeService;
    private final IProvince provinceService;
    private final ICity cityService;
    private final IArea areaService;
    private final ISysConfig sysConfigService;

    public Cachelistener(CacheUtil cacheUtil, IStoreEmployee employeeService, IProvince provinceService, ICity cityService, IArea areaService,ISysConfig sysConfigService) {
        this.cacheUtil = cacheUtil;
        this.employeeService = employeeService;
        this.provinceService = provinceService;
        this.cityService = cityService;
        this.areaService = areaService;
        this.sysConfigService=sysConfigService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("--------------------- init system cache ---------------------------");
        //加载employee到缓存中
        LoadEmployee();
        //加载省份列表到缓存
        LoadProvince();
        //加载城市列表到缓存
        LoadCity();
        //加载区域列表到缓存
        LoadArea();
        //加载系统配置到缓存
        LoadSysConfig();
        log.info("--------------------- cache init completed ------------------------");
    }

    private void LoadEmployee(){
        log.info("--------------------- add employee to cache -----------------------");
        List<StoreEmployee> employeeList = employeeService.findAll();
        Map<String, StoreEmployee> employeeMap = new HashMap<>(employeeList.size());
        for (StoreEmployee employee : employeeList) {
            employeeMap.put(employee.getUserId(), employee);
        }
        cacheUtil.Put(CacheKey.StoreEmployee, employeeMap);
        log.info("--------------------- employee added ------------------------------");
    }

    private void LoadProvince(){
        log.info("--------------------- add province to cache -----------------------");
        List<Province> provinceList = provinceService.findAll();
        cacheUtil.Put(CacheKey.Province, provinceList);
        log.info("--------------------- province added ------------------------------");
    }

    private void LoadCity(){
        log.info("--------------------- add city to cache ---------------------------");
        List<City> cityList = cityService.findAll();
        cacheUtil.Put(CacheKey.City, cityList);
        log.info("--------------------- city added ----------------------------------");
    }
    private void LoadArea(){
        log.info("--------------------- add area to cache ---------------------------");
        List<Area> areaList = areaService.findAll();
        cacheUtil.Put(CacheKey.Area, areaList);
        log.info("--------------------- area added ----------------------------------");
    }
    private void LoadSysConfig(){
        log.info("--------------------- add system config to cache ---------------------------");
        List<SysConfig> configs = sysConfigService.FindAll();
        Map<String,String> configMap=new HashMap<>(configs.size());
        for(SysConfig config:configs){
            configMap.put(config.getKey(),config.getValue());
        }
        cacheUtil.Put(CacheKey.SysConfig, configMap);
        log.info("--------------------- system config added ----------------------------------");
    }
}
