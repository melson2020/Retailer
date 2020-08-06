package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.webserver.entity.ProductCategory;
import com.melson.webserver.entity.Supply;
import com.melson.webserver.service.ISupply;
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
@RequestMapping("/supply")
public class SupplyResource extends BaseResource {
    private final ISupply supply;


    public SupplyResource(ISupply supply) {
        this.supply = supply;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result RegisterStore(HttpServletRequest request){
        Result result=new Result();
        List<Supply> supplies = supply.findAll();
        result.setData(supplies);
        System.out.println("POST Rest Call: /supply/list ...");
        return  result;
    }
}
