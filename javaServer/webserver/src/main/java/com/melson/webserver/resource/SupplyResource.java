package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.entity.StoreEmployee;
import com.melson.webserver.entity.ProductCategory;
import com.melson.webserver.entity.Supply;
import com.melson.webserver.service.ISupply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final ISupply supplyService;

    public SupplyResource(ISupply supply) {
        this.supplyService = supply;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result GetSupplyList(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(storeCode)){
            return this.GenerateResult(ResultType.ParametersNeeded);
        }
        List<Supply> supplies = supplyService.findSupplyByStoreCode(storeCode);
        Result result=new Result();
        result.setData(supplies);
        System.out.println("POST Rest Call: /supply/list ...");
        return  result;
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Result GetSupplyListAll(HttpServletRequest request){
        Result result=new Result();
        List<Supply> supplies = supplyService.findAll();
        result.setData(supplies);
        System.out.println("POST Rest Call: /supply/all ...");
        return  result;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result CreateSupply(@RequestBody Supply supply){
        Result result=new Result();
        Supply saved=supplyService.SaveSupply(supply);
        if(saved==null){
            result.setResultStatus(-1);
            result.setMessage("create fail");
        }else {
            result.setData(saved);
        }
        return result;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Result DeleteSupply(@RequestBody Supply supply){
        Result result=new Result();
        Integer deleteCount=supplyService.DeleteSupply(supply);
        result.setResultStatus(deleteCount>0?1:-1);
        result.setData(deleteCount);
        return result;
    }

    @RequestMapping(value = "/query",method = RequestMethod.POST)
    public Result QuerySupply(@RequestBody Supply supply){
        Supply sup=supplyService.Query(supply);
        Result result=new Result();
        result.setData(sup);
        return result;
    }

}
