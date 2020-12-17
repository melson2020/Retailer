package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.base.Result;
import com.melson.base.entity.StoreEmployee;
import com.melson.webserver.dao.ISupplyDao;
import com.melson.webserver.entity.Supply;
import com.melson.webserver.service.ISupply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Supply SaveSupply(Supply supply) {
        Supply saved=supplyDao.save(supply);
        return saved;
    }

    @Override
    public Result SaveAndUpdate(Supply supply) {
        Result result = new Result();
        //判断是否存在相同名称
        Supply checkExist=CheckExisting(supply.getName(),supply.getStoreCode());
        if(checkExist!=null){
            if(supply.getId()==checkExist.getId()){
                Supply saved=supplyDao.save(supply);
                if(saved==null){
                    result.setResultStatus(-1);
                    result.setMessage("保存失败！");
                }else {
                    result.setData(saved);
                }
            }
            else
            {
                result.setResultStatus(-1);
                result.setMessage("已经存在此供应商名称！");
            }

        }
        else
        {
            Supply saved=supplyDao.save(supply);
            if(saved==null){
                result.setResultStatus(-1);
                result.setMessage("保存失败！");
            }else {
                result.setData(saved);
            }
        }
        return result;
    }

    //检查是否重名
    private Supply CheckExisting(String name,String storeCode) {
        Supply supply=supplyDao.findByNameAndStoreCode(name,storeCode);
        return supply;
    }


    @Override
    @Transactional
    public Integer DeleteSupply(Supply supply) {
        return supplyDao.deleteBySupplyId(supply.getId());
    }

    @Override
    public Supply Query(Supply supply) {
        Supply sup=supplyDao.findById(supply.getId());
        return sup;
    }


}
