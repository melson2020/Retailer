package com.melson.webserver.dao;

import com.melson.webserver.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
@Repository
public interface ISupplyDao extends JpaRepository<Supply,String> {

//    @Query(nativeQuery=true,value="select s.id,s.name,s.address,s.contact,s.phone from supply s INNER JOIN supply_relation sr on s.id=sr.supplyId where sr.storeCode=?1")
//    List<Object[]> findByStoreCode(String storeCode);

    List<Supply> findAllByStoreCode(String storeCode);

    @Modifying
    @Query("delete from Supply su where su.id=?1")
    int deleteBySupplyId(Integer id);

    Supply findById(Integer id);
}
