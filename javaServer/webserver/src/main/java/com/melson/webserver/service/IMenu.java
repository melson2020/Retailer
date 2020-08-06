package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.entity.Menu;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/6
 */
public interface IMenu extends IService<Menu> {
    List<Menu> GetMenuListWithPermission(Integer permissionLevel);
}
