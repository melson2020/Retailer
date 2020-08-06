package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.ISubMenuDao;
import com.melson.webserver.entity.SubMenu;
import com.melson.webserver.service.ISubMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/6
 */
@Service
public class SubMenuImpl extends AbstractService<SubMenu> implements ISubMenu {
    private final ISubMenuDao subMenuDao;

    public SubMenuImpl(ISubMenuDao subMenuDao) {
        this.subMenuDao = subMenuDao;
    }

    @Override
    public JpaRepository<SubMenu, String> getRepository() {
        return subMenuDao;
    }
}
