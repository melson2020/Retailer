package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IMenuToSubMenuDao;
import com.melson.webserver.entity.MenuToSubMenu;
import com.melson.webserver.service.IMenuToSubMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/6
 */
@Service
public class MenuToSubMenuImpl extends AbstractService<MenuToSubMenu> implements IMenuToSubMenu {
    private final IMenuToSubMenuDao menuToSubMenuDao;

    public MenuToSubMenuImpl(IMenuToSubMenuDao menuToSubMenuDao) {
        this.menuToSubMenuDao = menuToSubMenuDao;
    }

    @Override
    public JpaRepository<MenuToSubMenu, String> getRepository() {
        return menuToSubMenuDao;
    }
}
