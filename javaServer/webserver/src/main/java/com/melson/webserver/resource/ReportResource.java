package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.webserver.Vo.StorageRecordVo;
import com.melson.webserver.service.IProductStorage;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/11/24
 */
@RestController
@RequestMapping(value = "/report")
public class ReportResource extends BaseResource {
    private final IProductStorage productStorageService;

    public ReportResource(IProductStorage productStorageService) {
        this.productStorageService = productStorageService;
    }

    @RequestMapping(value = "/productStorageRec")
    public Result FindProductStorageRecord(HttpServletRequest request) {
        Result result = new Result();
        String productId=request.getParameter("productId");
        String storeCode=request.getParameter("storeCode");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        if(StringUtils.isEmpty(productId)||StringUtils.isEmpty(storeCode)||StringUtils.isEmpty(startDate)||StringUtils.isEmpty(endDate))return this.GenerateResult(ResultType.ParametersNeeded);
        try {
            Integer pid=Integer.parseInt(productId);
            List<StorageRecordVo> list= productStorageService.FindProductStorageRec(pid,startDate,endDate,storeCode);
            result.setData(list);
            return result;
        }catch (Exception ex){
            result.setResultStatus(-1);
            result.setMessage(ex.getMessage());
            return result;
        }
    }
}
