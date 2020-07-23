package com.melson.base.dao;

import com.melson.base.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Nelson on 2020/7/23.
 */
@Repository
public interface IPermissionDao extends JpaRepository<Permission,String> {
}
