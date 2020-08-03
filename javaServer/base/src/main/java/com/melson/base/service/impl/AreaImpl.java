package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.cache.CacheKey;
import com.melson.base.cache.CacheUtil;
import com.melson.base.dao.IAreaDao;
import com.melson.base.entity.Area;
import com.melson.base.service.IArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class AreaImpl extends AbstractService<Area> implements IArea {
    private final IAreaDao areaDao;
    private final CacheUtil cacheUtil;
    public AreaImpl(IAreaDao areaDao,CacheUtil cacheUtil) {
        this.areaDao = areaDao;
        this.cacheUtil=cacheUtil;
    }

    @Override
    public JpaRepository<Area, String> getRepository() {
        return areaDao;
    }

    @Override
    public List<Area> findAll() {
        return areaDao.findAll();
    }

    @Override
    public List<Area> findWithCityCode(String cityCode) {
        List<Area> areaList=GetAreaList();
        List<Area> result=new ArrayList<>();
        for (Area area:areaList){
            if(area.getCityCode().equals(cityCode))result.add(area);
        }
        return result;
    }

    private List<Area> GetAreaList(){
        List<Area> areaList=cacheUtil.GetObjectValue(CacheKey.Area,List.class);
        if(areaList.isEmpty()||areaList.size()<=0){
            areaList=areaDao.findAll();
            cacheUtil.Put(CacheKey.Area,areaList);
        }
        return areaList;
    }
}
