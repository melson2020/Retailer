package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.entity.StorageOutTicket;
import com.melson.webserver.service.IStorageOutTicket;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public GoodsReturnResource(IStorageOutTicket outTicketService) {
        this.outTicketService = outTicketService;
    }

    @RequestMapping(value = "/findOutTickets")
    @RequiredPermission(SecurityLevel.Employee)
    public Result FindStorageOutTicket(HttpServletRequest request){
        String searchValue=request.getParameter("searchValue");
        String date=request.getParameter("date");
        if(StringUtils.isEmpty(searchValue)&&StringUtils.isEmpty(date))return this.GenerateResult(ResultType.ParametersNeeded);
        Result result=new Result();
        List<StorageOutTicket> tickets=outTicketService.FindTicketsWithCodeOrCustomerNameAndDate(searchValue,date);
        result.setData(tickets);
        return result;
    }
}
