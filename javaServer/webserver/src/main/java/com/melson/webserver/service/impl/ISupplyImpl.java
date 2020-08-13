package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.entity.StoreEmployee;
import com.melson.webserver.dao.ISupplyDao;
import com.melson.webserver.entity.Supply;
import com.melson.webserver.service.ISupply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
@Service
public class ISupplyImpl extends AbstractService <Supply> implements ISupply {
    private final ISupplyDao supplyDao;

    public ISupplyImpl(ISupplyDao supplyDao) {
        this.supplyDao = supplyDao;
    }

    @Override
    public JpaRepository<Supply, String> getRepository() {
        return supplyDao;
    }

    @Override
    public List<Supply> findAll() {
        return supplyDao.findAll();
    }

    @Override
    public List<Supply> findSupplyByStoreCode(String storeCode) {
        List<Supply> supplyList=supplyDao.findAllByStoreCode(storeCode);
//        List<Supply> supplyList=new ArrayList<>();
//        List<Object[]> supplyVo = supplyDao.findByStoreCode(storeCode);
//        for(Object[] obj:supplyVo){
//            Supply sp=new Supply();
//            sp.setId(obj[0]==null?null:new Integer((Integer) obj[0]));
//            sp.setName(obj[1]==null?null:obj[1].toString());
//            sp.setAddress(obj[2]==null?null:obj[2].toString());
//            sp.setContact(obj[3]==null?null:obj[3].toString());
//            sp.setPhone(obj[4]==null?null:obj[4].toString());
//            supplyList.add(sp);
//        }
        return supplyList;
    }

    @Override
    public Supply CreateSupply(Supply supply) {
        Supply saved=supplyDao.save(supply);
        return saved;
    }


}
