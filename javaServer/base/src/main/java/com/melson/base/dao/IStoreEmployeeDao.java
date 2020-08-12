package com.melson.base.dao;

import com.melson.base.entity.StoreEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nelson on 2020/7/23.
 */
@Repository
public interface IStoreEmployeeDao extends JpaRepository<StoreEmployee,String> {
    StoreEmployee findByLoginNameAndPassword(String loginName,String password);
    List<StoreEmployee> findAllByStoreCode(String storeCode);
    StoreEmployee findByLoginName(String loginName);
    @Modifying
    @Query("UPDATE StoreEmployee se set  se.userName=?1,se.phone=?2,se.gender=?3,se.permission=?4 where se.userId=?5")
    int updateEmployee(String userName,String phone,Integer gender,Integer permission,String userId);

    @Modifying
    @Query("delete from StoreEmployee se where se.userId=?1")
    int deleteByUserId(String userId);

    @Modifying
    @Query("UPDATE StoreEmployee se set se.password=?4, se.loginName=?2  where se.userId=?1 and se.password=?3")
    int reSetPass(String userId,String loginName,String oldPass,String newPass);
}
