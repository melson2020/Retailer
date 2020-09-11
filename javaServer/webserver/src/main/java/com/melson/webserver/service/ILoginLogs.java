package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.base.entity.StoreEmployee;
import com.melson.webserver.entity.LoginLogs;

/**
 * Created by Messi on 2020/8/31
 */
public interface ILoginLogs extends IService<LoginLogs> {
    void Records(StoreEmployee employee);
}
