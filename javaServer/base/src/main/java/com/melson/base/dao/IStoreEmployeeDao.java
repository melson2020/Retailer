package com.melson.base.dao;

import com.melson.base.entity.StoreEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
@Repository
public interface IStoreEmployeeDao extends JpaRepository<StoreEmployee,String> {
}
