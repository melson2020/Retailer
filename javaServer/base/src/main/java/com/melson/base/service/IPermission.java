package com.melson.base.service;

import com.melson.base.IService;
import com.melson.base.entity.Permission;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
public interface IPermission extends IService<Permission> {
    List<Permission> findAll();
}
