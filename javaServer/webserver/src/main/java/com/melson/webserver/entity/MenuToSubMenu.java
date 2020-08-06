package com.melson.webserver.entity;

import javax.persistence.*;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/6
 */
@Entity
@Table(name = "sys_menu_submenu")
public class MenuToSubMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer menuId;
    private Integer submenuId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getSubmenuId() {
        return submenuId;
    }

    public void setSubmenuId(Integer submenuId) {
        this.submenuId = submenuId;
    }
}
