package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.IPermissionDao;
import com.melson.base.entity.Permission;
import com.melson.base.service.IPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class PermissionImpl extends AbstractService<Permission> implements IPermission {
    private final IPermissionDao permissionDao;

    public PermissionImpl(IPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    @Override
    public JpaRepository<Permission, String> getRepository() {
        return permissionDao;
    }
}
