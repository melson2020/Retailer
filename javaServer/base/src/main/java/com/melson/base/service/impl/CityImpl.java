package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.ICityDao;
import com.melson.base.entity.City;
import com.melson.base.service.ICity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class CityImpl extends AbstractService<City> implements ICity {
    private final ICityDao cityDao;

    public CityImpl(ICityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    public JpaRepository<City, String> getRepository() {
        return cityDao;
    }

    @Override
    public List<City> findAll() {
        return cityDao.findAll();
    }
}
