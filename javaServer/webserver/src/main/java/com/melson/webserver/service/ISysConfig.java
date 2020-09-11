package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.entity.SysConfig;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/4
 */
public interface ISysConfig extends IService<SysConfig> {
    List<SysConfig> FindAll();
    String FindValueFromCache(String key);
}
