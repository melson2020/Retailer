package com.melson.base.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.dao.IAreaDao;
import com.melson.base.entity.Area;
import com.melson.base.service.IArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Nelson on 2020/7/23.
 */
@Service
public class AreaImpl extends AbstractService<Area> implements IArea {
    private final IAreaDao areaDao;
    public AreaImpl(IAreaDao areaDao) {
        this.areaDao = areaDao;
    }

    @Override
    public JpaRepository<Area, String> getRepository() {
        return areaDao;
    }
}
