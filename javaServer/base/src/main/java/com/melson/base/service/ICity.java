package com.melson.base.service;

import com.melson.base.IService;
import com.melson.base.entity.City;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
public interface ICity extends IService<City> {
    List<City> findAll();
    List<City> findWithProvinceCode(String code);
}
