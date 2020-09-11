package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.Vo.StorageAndProductCountVo;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.service.IProductStorage;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/8/27
 */
@RestController
@RequestMapping(value = "/storage")
public class ProductStorageResource extends BaseResource {
    private final IProductStorage productStorageService;

    public ProductStorageResource(IProductStorage productStorageService) {
        this.productStorageService = productStorageService;
    }

    @RequestMapping(value = "/storageAndProductCount")
    @RequiredPermission(SecurityLevel.Manager)
    public Result GetStrogaeInfo(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(storeCode)) return  this.GenerateResult(ResultType.ParametersNeeded);
        Result result=new Result();
        StorageAndProductCountVo vo=productStorageService.GetProductAndStorageCount(storeCode);
        result.setData(vo);
        return result;
    }

    @RequestMapping(value = "/generateStorage")
    @RequiredPermission(SecurityLevel.Manager)
    public Result GenerateStorage(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(storeCode)) return  this.GenerateResult(ResultType.ParametersNeeded);
        Result result=new Result();
        List<ProductStorage> list=productStorageService.GenerateStorage(storeCode);
        if(list!=null){
            result.setData(list);
        }else {
            result.setResultStatus(-1);
            result.setMessage("Generate failed");
        }
        return  result;
    }

    @RequestMapping(value = "/storageList")
    @RequiredPermission(SecurityLevel.Manager)
    public Result GetStorageList(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(storeCode)) return  this.GenerateResult(ResultType.ParametersNeeded);
        Result result=new Result();
        List<ProductStorage> list=productStorageService.FindAll(storeCode);
        result.setData(list);
        return  result;
    }

    @RequestMapping(value = "/storageDetail")
    @RequiredPermission(SecurityLevel.Manager)
    public Result GetStorageDetail(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        String productId=request.getParameter("productId");
        if(StringUtils.isEmpty(storeCode)||StringUtils.isEmpty(productId)) return  this.GenerateResult(ResultType.ParametersNeeded);
        Integer pid=Integer.parseInt(productId);
        Result result=new Result();
        List<ProductBatch> list=productStorageService.FindBatchList(storeCode,pid);
        result.setData(list);
        return  result;
    }
}
