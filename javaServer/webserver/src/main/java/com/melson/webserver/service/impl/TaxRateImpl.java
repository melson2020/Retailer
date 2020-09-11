package com.melson.webserver.service.impl;

import com.melson.base.AbstractService;
import com.melson.webserver.dao.ITaxRateDao;
import com.melson.webserver.entity.TaxRate;
import com.melson.webserver.service.ITaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/4
 */
@Service
public class TaxRateImpl extends AbstractService<TaxRate> implements ITaxRate {
    private final ITaxRateDao taxRateDao;

    public TaxRateImpl(ITaxRateDao taxRateDao) {
        this.taxRateDao = taxRateDao;
    }

    @Override
    public JpaRepository<TaxRate, String> getRepository() {
        return taxRateDao;
    }

    @Override
    public List<TaxRate> FindTaxRateList() {
        return taxRateDao.findAll();
    }
}
