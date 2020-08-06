package com.melson.webserver.dao;

import com.melson.webserver.entity.MenuToSubMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/6
 */
@Repository
public interface IMenuToSubMenuDao extends JpaRepository<MenuToSubMenu,String> {
}
