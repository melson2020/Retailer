package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.IEmployeePermissionDao;
import com.melson.base.entity.EmployeePermission;
import com.melson.base.service.IEmployeePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class EmployeePermissionImpl extends AbstractService<EmployeePermission> implements IEmployeePermission {
    private final IEmployeePermissionDao employeePermissionDao;

    public EmployeePermissionImpl(IEmployeePermissionDao employeePermissionDao) {
        this.employeePermissionDao = employeePermissionDao;
    }

    @Override
    public JpaRepository<EmployeePermission, String> getRepository() {
        return employeePermissionDao;
    }
}
