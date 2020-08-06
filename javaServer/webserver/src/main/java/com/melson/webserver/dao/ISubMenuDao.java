package com.melson.webserver.dao;

import com.melson.webserver.entity.SubMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/6
 */
@Repository
public interface ISubMenuDao extends JpaRepository<SubMenu,String> {
    @Query("FROM SubMenu sb where sb.permission <=?1")
    List<SubMenu> findUnderPermission(Integer permission);
}
