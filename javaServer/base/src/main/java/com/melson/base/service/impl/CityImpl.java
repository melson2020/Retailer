package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.cache.CacheKey;
import com.melson.base.cache.CacheUtil;
import com.melson.base.dao.ICityDao;
import com.melson.base.entity.City;
import com.melson.base.service.ICity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class CityImpl extends AbstractService<City> implements ICity {
    private final ICityDao cityDao;
    private final CacheUtil cacheUtil;

    public CityImpl(ICityDao cityDao,CacheUtil cacheUtil) {
        this.cityDao = cityDao;
        this.cacheUtil=cacheUtil;
    }

    @Override
    public JpaRepository<City, String> getRepository() {
        return cityDao;
    }

    @Override
    public List<City> findAll() {
        return cityDao.findAll();
    }

    @Override
    public List<City> findWithProvinceCode(String code) {
       List<City> cityList=GetCityList();
       List<City> result=new ArrayList<>();
       for (City city:cityList){
           if(city.getProvinceCode().equals(code))result.add(city);
       }
       return result;
    }

    private List<City> GetCityList(){
        List<City> cityList=cacheUtil.GetObjectValue(CacheKey.City,List.class);
        if(cityList.isEmpty()||cityList.size()<=0){
            cityList=cityDao.findAll();
            cacheUtil.Put(CacheKey.City,cityList);
        }
        return cityList;
    }
}
