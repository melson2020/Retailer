package com.melson.webserver.service;

import com.melson.base.IService;
import com.melson.webserver.entity.TaxRate;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/4
 */
public interface ITaxRate extends IService<TaxRate> {
    List<TaxRate> FindTaxRateList();
}
