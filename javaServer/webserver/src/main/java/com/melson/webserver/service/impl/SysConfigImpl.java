package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.cache.CacheKey;
import com.melson.base.cache.CacheUtil;
import com.melson.webserver.dao.ISysConfigDao;
import com.melson.webserver.entity.SysConfig;
import com.melson.webserver.service.ISysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/4
 */
@Service
public class SysConfigImpl extends AbstractService<SysConfig> implements ISysConfig {
    private final ISysConfigDao sysConfigDao;
    private final CacheUtil cacheUtil;

    public SysConfigImpl(ISysConfigDao sysConfigDao,CacheUtil cacheUtil) {
        this.sysConfigDao = sysConfigDao;
        this.cacheUtil=cacheUtil;
    }

    @Override
    public JpaRepository<SysConfig, String> getRepository() {
        return null;
    }

    @Override
    public List<SysConfig> FindAll() {
        return sysConfigDao.findAll();
    }

    @Override
    public String FindValueFromCache(String key) {
        Map<String,String> configMap=cacheUtil.GetObjectValue(CacheKey.SysConfig,Map.class);
        return configMap.get(key);
    }
}
