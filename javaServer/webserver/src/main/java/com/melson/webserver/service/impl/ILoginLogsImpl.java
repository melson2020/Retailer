package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.entity.StoreEmployee;
import com.melson.webserver.dao.ILoginLogsDao;
import com.melson.webserver.entity.LoginLogs;
import com.melson.webserver.service.ILoginLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Messi on 2020/8/31
 */
@Service
public class ILoginLogsImpl extends AbstractService<LoginLogs> implements ILoginLogs {
    private final ILoginLogsDao loginLogsDao;
    public ILoginLogsImpl(ILoginLogsDao loginLogsDao){
        this.loginLogsDao=loginLogsDao;
    }
    @Override
    public JpaRepository<LoginLogs, String> getRepository() {
        return null;
    }

    @Override
    public void Records(StoreEmployee employee) {
        LoginLogs logs= new LoginLogs();
        logs.setLogs(employee.getLoginFrom());
        logs.setLogDate(new Date());
        loginLogsDao.save(logs);
    }
}
