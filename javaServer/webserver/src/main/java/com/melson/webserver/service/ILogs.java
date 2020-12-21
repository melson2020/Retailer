package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.entity.StoreEmployee;
import com.melson.webserver.entity.Logs;

/**
 * Created by Messi on 2020/8/31
 */
public interface ILogs extends IService<Logs> {
    void Records(StoreEmployee employee);
    Logs FindBylogs(String log);
}
