package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.cache.CacheUtil;
import com.melson.base.entity.Store;
import com.melson.webserver.dao.IStoreSharePathDao;
import com.melson.webserver.entity.StoreSharePath;
import com.melson.webserver.service.IStoreSharePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @Author Nelson
 * @Description
 * @Date 2021/1/6
 */
@Service
public class IStoreSharePathImpl extends AbstractService<StoreSharePath> implements IStoreSharePath {
    private final IStoreSharePathDao storeSharePathDao;

    public IStoreSharePathImpl(IStoreSharePathDao storeSharePathDao) {
        this.storeSharePathDao = storeSharePathDao;
    }

    @Override
    public JpaRepository<StoreSharePath, String> getRepository() {
        return storeSharePathDao;
    }

    /**
     * 创建分享链接
     *
     * @param storeCode
     * @param parentPath
     * @return
     */
    @Override
    public StoreSharePath CreateSharePath(String storeCode, String parentPath) {
        StoreSharePath storeSharePath = new StoreSharePath();
        storeSharePath.setStoreCode(storeCode);
        String code = UUID.randomUUID().toString();
        storeSharePath.setShareCode(code);
        String sharePath = parentPath + code;
        storeSharePath.setSharePath(sharePath);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        date = calendar.getTime();
        storeSharePath.setExpireDate(date);
        return storeSharePathDao.save(storeSharePath);
    }

    @Override
    public StoreSharePath FindSharePath(String storeCode) {
        return storeSharePathDao.findByStoreCode(storeCode);
    }

    @Override
    public StoreSharePath FindSharePathWithShareCode(String shareCode) {
        return storeSharePathDao.findByShareCode(shareCode);
    }
}
