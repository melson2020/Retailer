package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.cache.CacheKey;
import com.melson.base.cache.CacheUtil;
import com.melson.base.dao.IPermissionDao;
import com.melson.base.dao.IProvinceDao;
import com.melson.base.entity.Province;
import com.melson.base.service.IProvince;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class ProvinceImpl extends AbstractService<Province> implements IProvince {
    private final IProvinceDao provinceDao;
    private final CacheUtil cacheUtil;

    public ProvinceImpl(IProvinceDao provinceDao,CacheUtil cacheUtil) {
        this.provinceDao = provinceDao;
        this.cacheUtil=cacheUtil;
    }

    @Override
    public JpaRepository<Province, String> getRepository() {
        return provinceDao;
    }

    @Override
    public List<Province> findAll() {
        return provinceDao.findAll();
    }

    @Override
    public List<Province> findAllFromCache() {
        return GetProvinceList();
    }


    public List<Province> GetProvinceList(){
        List<Province> provinceList=cacheUtil.GetObjectValue(CacheKey.Province,List.class);
        if(provinceList.isEmpty()||provinceList.size()<=0){
            provinceList=provinceDao.findAll();
            cacheUtil.Put(CacheKey.Province,provinceList);
        }
        return provinceList;
    }

}
