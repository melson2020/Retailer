package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.webserver.entity.Supply;
import com.melson.webserver.entity.SupplyProduct;
import com.melson.webserver.service.ISupplyProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Messi on 2020/8/6
 */
@RestController
@RequestMapping("/supplyProduct")
public class SupplyProductResource extends BaseResource {
    private final ISupplyProduct supplyProduct;

    public SupplyProductResource(ISupplyProduct supplyProduct) {
        this.supplyProduct = supplyProduct;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result RegisterStore(HttpServletRequest request){
        Result result=new Result();
        List<SupplyProduct> supplyProducts = supplyProduct.findAll();
        result.setData(supplyProducts);
        System.out.println("POST Rest Call: /supplyProduct/list ...");
        return  result;
    }
}
