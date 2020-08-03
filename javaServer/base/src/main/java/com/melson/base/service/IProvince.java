package com.melson.base.service;

import com.melson.base.IService;
import com.melson.base.entity.Province;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
public interface IProvince extends IService<Province> {
    List<Province> findAll();
    List<Province> findAllFromCache();
}
