package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.Vo.StoreInTicketVo;
import com.melson.webserver.entity.StorageInDetail;
import com.melson.webserver.entity.StorageInTicket;
import com.melson.webserver.entity.TaxRate;
import com.melson.webserver.service.IStorageInTicket;
import com.melson.webserver.service.ITaxRate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
@RestController
@RequestMapping(value = "/storageIn")
public class StorageInResource extends BaseResource {
    private final IStorageInTicket storageInTicketService;
    private final ITaxRate taxRateService;

    public StorageInResource(IStorageInTicket storageInTicketService, ITaxRate taxRateService) {
        this.storageInTicketService = storageInTicketService;
        this.taxRateService = taxRateService;
    }

    @RequestMapping(value = "/saveTicket", method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result SaveStorageInTicket(@RequestBody StorageInTicket ticket) {
        if (StringUtils.isEmpty(ticket.getStoreCode())) return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        Integer res = storageInTicketService.SaveStorageInTicket(ticket);
        result.setResultStatus(res);
        result.setMessage(res == 1 ? "" : res == -1 ? "update storage fail" : "save record fail");
        return result;
    }

    @RequestMapping(value = "/taxRateList", method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetTaxRateList(HttpServletRequest request) {
        Result result = new Result();
        List<TaxRate> taxRateList = taxRateService.FindTaxRateList();
        result.setData(taxRateList);
        return result;
    }

    @RequestMapping(value = "/storageInRecord", method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetStorageInRecordList(HttpServletRequest request) {
        String storeCode = request.getParameter("storeCode");
        if (StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        List<StoreInTicketVo> ticketList = storageInTicketService.FindTickets(startDate, endDate, storeCode);
        Result result = new Result();
        if (ticketList == null) {
            result.setResultStatus(-1);
            result.setMessage("Exception cached,please confirm date format");
        } else {
            Collections.sort(ticketList);
            result.setData(ticketList);
        }
        return result;
    }

    @RequestMapping(value = "/storageInRecordDetails", method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetStorageInRecordDetails(HttpServletRequest request){
        String code=request.getParameter("storeInTicketCode");
        if (StringUtils.isEmpty(code)) return this.GenerateResult(ResultType.ParametersNeeded);
        List<StorageInDetail> details=storageInTicketService.FindTicketDetails(code);
        Result result=new Result();
        if(details==null){
            result.setResultStatus(-1);
            result.setMessage("result list is null");
        }else {
            result.setData(details);
        }
        return  result;
    }

}
