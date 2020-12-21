package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.entity.StoreEmployee;
import com.melson.webserver.dao.ILogsDao;
import com.melson.webserver.entity.Logs;
import com.melson.webserver.service.ILogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Messi on 2020/8/31
 */
@Service
public class ILogsImpl extends AbstractService<Logs> implements ILogs {
    private final ILogsDao loginLogsDao;
    public ILogsImpl(ILogsDao loginLogsDao){
        this.loginLogsDao=loginLogsDao;
    }
    @Override
    public JpaRepository<Logs, String> getRepository() {
        return null;
    }

    @Override
    public void Records(StoreEmployee employee) {
        Logs logs= new Logs();
        logs.setLogs(employee.getLoginFrom());
        logs.setLogDate(new Date());
        loginLogsDao.save(logs);
    }

    @Override
    public Logs FindBylogs(String log) {
        return loginLogsDao.findWithQuery(log);
    }
}
