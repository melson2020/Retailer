package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.webserver.entity.SupplyProduct;
import com.melson.webserver.entity.SupplyRelation;
import com.melson.webserver.service.ISupplyRelation;
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
@RequestMapping("/supplyRelation")
public class SupplyRelationResource extends BaseResource {
    private final ISupplyRelation supplyRelation;

    public SupplyRelationResource(ISupplyRelation supplyRelation) {
        this.supplyRelation = supplyRelation;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result RegisterStore(HttpServletRequest request){
        Result result=new Result();
        List<SupplyRelation> supplyRelations = supplyRelation.findAll();
        result.setData(supplyRelations);
        System.out.println("POST Rest Call: /supplyRelation/list ...");
        return  result;
    }

}
