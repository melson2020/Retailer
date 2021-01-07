package com.melson.webserver.Vo;

import com.melson.base.entity.Store;
import com.melson.webserver.entity.Menu;
import com.melson.webserver.entity.ProductStorage;

import java.util.List;

/**
 * Created by Messi on 2021/1/6
 */
public class ShareProductListVo {
    Store store;
    List<Menu> menuList;
    List<ShareStorageVo> productStorageList;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<ShareStorageVo> getProductStorageList() {
        return productStorageList;
    }

    public void setProductStorageList(List<ShareStorageVo> productStorageList) {
        this.productStorageList = productStorageList;
    }
}
