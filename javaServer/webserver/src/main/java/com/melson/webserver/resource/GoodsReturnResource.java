package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.Vo.GoodsReturnVo;
import com.melson.webserver.entity.GoodsReturnRecord;
import com.melson.webserver.entity.StorageOutDetail;
import com.melson.webserver.entity.StorageOutTicket;
import com.melson.webserver.service.IGoodsReturnRecord;
import com.melson.webserver.service.IStorageOutTicket;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/15
 */
@RestController
@RequestMapping(value = "/goodsReturn")
public class GoodsReturnResource extends BaseResource {
    private final IStorageOutTicket outTicketService;
    private final IGoodsReturnRecord goodsReturnRecordService;

    public GoodsReturnResource(IStorageOutTicket outTicketService, IGoodsReturnRecord goodsReturnRecordService) {
        this.outTicketService = outTicketService;
        this.goodsReturnRecordService = goodsReturnRecordService;
    }

    @RequestMapping(value = "/findOutTickets")
    @RequiredPermission(SecurityLevel.Employee)
    public Result FindStorageOutTicket(HttpServletRequest request){
        String searchValue=request.getParameter("searchValue");
        String date=request.getParameter("date");
        String storeCode=request.getParameter("storeCode");
        if(StringUtils.isEmpty(searchValue)&&StringUtils.isEmpty(date))return this.GenerateResult(ResultType.ParametersNeeded);
        Result result=new Result();
        List<StorageOutTicket> tickets=outTicketService.FindTicketsWithCodeOrCustomerNameAndDate(searchValue,date,storeCode);
        result.setData(tickets);
        System.out.println("Rest Call: /goodsReturn/findOutTickets ...");
        return result;
    }

    @RequestMapping(value = "/findOutTicketDetails")
    @RequiredPermission(SecurityLevel.Employee)
    public Result FindStorageOutTicketDetail(HttpServletRequest request){
        String storeCode=request.getParameter("storeCode");
        String ticketCode=request.getParameter("ticketCode");
        if(StringUtils.isEmpty(storeCode)&&StringUtils.isEmpty(ticketCode))return this.GenerateResult(ResultType.ParametersNeeded);
        Result result=new Result();
        StorageOutTicket outTicket=outTicketService.FindTicketForGoodsReturn(storeCode,ticketCode);
        result.setData(outTicket);
        return result;
    }

    @RequestMapping(value = "/saveGoodsReturnRecord",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result SaveGoodsReturnRecord(@RequestBody GoodsReturnVo vo, HttpServletRequest request){
        List<GoodsReturnRecord> records=vo.getRecords();
        List<StorageOutDetail> details=vo.getOutDetails();
        if(records.size()<=0||details.size()<=0)return this.GenerateResult(ResultType.ParametersNeeded);
        System.out.println("Rest Call: /goodsReturn/saveGoodsReturnRecord ...");
        return goodsReturnRecordService.SaveGoodsReturnRecords(records,details);
    }
}
