package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.IMenuDao;
import com.melson.webserver.dao.IMenuToSubMenuDao;
import com.melson.webserver.dao.ISubMenuDao;
import com.melson.webserver.entity.Menu;
import com.melson.webserver.entity.MenuToSubMenu;
import com.melson.webserver.entity.SubMenu;
import com.melson.webserver.service.IMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/6
 */
@Service
public class MenuImpl extends AbstractService<Menu> implements IMenu {
    private final IMenuDao menuDao;
    private final ISubMenuDao subMenuDao;
    private final IMenuToSubMenuDao menuToSubMenuDao;

    public MenuImpl(IMenuDao menuDao, ISubMenuDao subMenuDao, IMenuToSubMenuDao menuToSubMenuDao) {
        this.menuDao = menuDao;
        this.subMenuDao = subMenuDao;
        this.menuToSubMenuDao = menuToSubMenuDao;
    }

    @Override
    public JpaRepository<Menu, String> getRepository() {
        return menuDao;
    }

    @Override
    public List<Menu> GetMenuListWithPermission(Integer permissionLevel) {
        List<Menu> menuList = menuDao.findUnderPermission(permissionLevel);
        List<SubMenu> subMenuList = subMenuDao.findUnderPermission(permissionLevel);
        Map<Integer, SubMenu> subMenuMap = new HashMap<>(subMenuList.size());
        for (SubMenu subMenu : subMenuList) {
            subMenuMap.put(subMenu.getId(), subMenu);
        }
        List<MenuToSubMenu> menuToSubMenus = menuToSubMenuDao.findAll();
        Map<Integer, List<Integer>> menuToSubMenuMap = new HashMap<>();
        for (MenuToSubMenu menuToSubMenu : menuToSubMenus) {
            Integer menuId = menuToSubMenu.getMenuId();
            List<Integer> subMenuIds = menuToSubMenuMap.get(menuId);
            if (subMenuIds == null) {
                subMenuIds = new ArrayList<>();
                subMenuIds.add(menuToSubMenu.getSubmenuId());
                menuToSubMenuMap.put(menuId, subMenuIds);
            } else {
                subMenuIds.add(menuToSubMenu.getSubmenuId());
            }
        }
        for (Menu menu : menuList) {
            List<Integer> subMenuIds = menuToSubMenuMap.get(menu.getId());
            if (subMenuIds != null & subMenuIds.size() > 1) {
                List<SubMenu> subMenus = new ArrayList<>();
                for(Integer subMenuId:subMenuIds){
                    SubMenu subMenu=subMenuMap.get(subMenuId);
                    if(subMenu!=null){
                        subMenus.add(subMenu);
                    }
                }
                menu.setSubMenus(subMenus);
            } else {
                continue;
            }
        }
        return menuList;
    }
}
