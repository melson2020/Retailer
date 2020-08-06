package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.entity.SupplyRelation;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
public interface ISupplyRelation extends IService<SupplyRelation> {
    List<SupplyRelation> findAll();
}
