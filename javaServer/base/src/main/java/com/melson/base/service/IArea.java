package com.melson.base.service;

import com.melson.base.IService;
import com.melson.base.entity.Area;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
public interface IArea extends IService<Area> {
    List<Area> findAll();
    List<Area> findWithCityCode(String cityCode);
}
