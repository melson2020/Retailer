package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.Vo.DashBoardVo;
import com.melson.webserver.Vo.StorageRecordVo;
import com.melson.webserver.entity.GoodsReturnRecord;
import com.melson.webserver.service.IGoodsReturnRecord;
import com.melson.webserver.service.IProductStorage;
import com.melson.webserver.service.IStorageOutTicket;
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
    private final IStorageOutTicket outTicketService;
    private final IGoodsReturnRecord goodsReturnRecordService;

    public ReportResource(IProductStorage productStorageService, IStorageOutTicket outTicketService, IGoodsReturnRecord goodsReturnRecordService) {
        this.productStorageService = productStorageService;
        this.outTicketService = outTicketService;
        this.goodsReturnRecordService = goodsReturnRecordService;
    }

    @RequestMapping(value = "/productStorageRec")
    @RequiredPermission(SecurityLevel.Employee)
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

    @RequestMapping(value = "/dashboard")
    @RequiredPermission(SecurityLevel.Employee)
    public Result GenerateDashboard(HttpServletRequest request) {
        Result result = new Result();
        String storeCode=request.getParameter("storeCode");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        if(StringUtils.isEmpty(storeCode)||StringUtils.isEmpty(startDate)||StringUtils.isEmpty(endDate))return this.GenerateResult(ResultType.ParametersNeeded);
        DashBoardVo vo=outTicketService.GenerateDashboard(storeCode,startDate,endDate);
        result.setData(vo);
        System.out.println("Rest Call: /report/dashboard ...");
        return result;
    }

    @RequestMapping(value = "/goodsReturnRecord")
    @RequiredPermission(SecurityLevel.Employee)
    public Result GoodsReturnRecord(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        if(StringUtils.isEmpty(storeCode)||StringUtils.isEmpty(startDate)||StringUtils.isEmpty(endDate))return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<GoodsReturnRecord> records=goodsReturnRecordService.FindRecords(storeCode,startDate,endDate);
        if(records==null){
            result.setResultStatus(-1);
            result.setMessage("Null result,please check date format");
        }else {
            result.setData(records);
        }
        return result;

    }
}
