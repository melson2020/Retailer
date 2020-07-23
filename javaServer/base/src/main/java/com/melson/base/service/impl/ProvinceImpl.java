package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.IPermissionDao;
import com.melson.base.dao.IProvinceDao;
import com.melson.base.entity.Province;
import com.melson.base.service.IProvince;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class ProvinceImpl extends AbstractService<Province> implements IProvince {
    private final IProvinceDao provinceDao;

    public ProvinceImpl(IProvinceDao provinceDao) {
        this.provinceDao = provinceDao;
    }

    @Override
    public JpaRepository<Province, String> getRepository() {
        return provinceDao;
    }
}
