package com.melson.webserver.dao;

import com.melson.webserver.entity.Menu;
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
public interface IMenuDao extends JpaRepository<Menu,String> {
    @Query("FROM Menu sb where sb.permission <=?1")
    List<Menu> findUnderPermission(Integer permission);
}
