package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.Vo.OutBoundDeliveryVo;
import com.melson.webserver.Vo.OutBoundVo;
import com.melson.webserver.Vo.StorageOutRecordVo;
import com.melson.webserver.Vo.StorageOutTicketDetailVo;
import com.melson.webserver.dto.StorageOutTiketDto;
import com.melson.webserver.entity.StorageOutTicket;
import com.melson.webserver.service.IStorageOutTicket;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/18
 */
@RestController
@RequestMapping(value = "/storageOut")
public class StorageOutResource extends BaseResource {
    private final IStorageOutTicket outTicketService;

    public StorageOutResource(IStorageOutTicket outTicketService) {
        this.outTicketService = outTicketService;
    }

    @RequestMapping(value = "/saveOutTicket",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result SaveStorageOutTicket(@RequestBody StorageOutTiketDto dto){
        Result result=new Result();
        boolean success=outTicketService.SaveStorageOutTiket(dto.getOutTicket(),dto.getBillDetailList());
        if(!success){
            result.setResultStatus(-1);
            result.setMessage("save failure");
        }
        System.out.println("Rest Call: /storageOut/saveOutTicket ...");
        return result;
    }

    @RequestMapping(value = "/ticketRecord",method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result FindOutTicketRecord(HttpServletRequest request){
        String storeCode = request.getParameter("storeCode");
        if (StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        List<StorageOutRecordVo> voList=outTicketService.FindRecordList(storeCode,startDate,endDate);
        Result result=new Result();
        result.setData(voList);
        System.out.println("Rest Call: /storageOut/ticketRecord ...");
        return result;
    }

    @RequestMapping(value = "/outboundList",method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Manager)
    public Result GetOutboundList(HttpServletRequest request){
        String storeCode = request.getParameter("storeCode");
        String permission = request.getParameter("permission");
        String userId = request.getParameter("userId");
        if (StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String customerId= request.getParameter("customerId");
        String productId= request.getParameter("productId");
        String employeeId= request.getParameter("employeeId");
        Result result=new Result();
        List<OutBoundVo> voList=outTicketService.FindOutBoundList(startDate,endDate,storeCode,permission,userId,customerId,productId,employeeId);
        OutBoundDeliveryVo vo=new OutBoundDeliveryVo(startDate,endDate,voList);
        result.setData(vo);
        System.out.println("Rest Call: /storageOut/outboundList ...");
        return result;
    }

    @RequestMapping(value ="/recordDetail",method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result FindRecordDetails(HttpServletRequest request){
        String ticketCode=request.getParameter("ticketCode");
        String billCode=request.getParameter("billCode");
        String storeCode=request.getParameter("storeCode");
        if (StringUtils.isEmpty(ticketCode)||StringUtils.isEmpty(billCode)||StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        StorageOutTicketDetailVo vo=outTicketService.FindRecordDetail(ticketCode,billCode,storeCode);
        Result result=new Result();
        result.setData(vo);
        System.out.println("Rest Call: /storageOut/recordDetail ...");
        return result;
    }

    @RequestMapping(value ="/ticketInfo",method = RequestMethod.GET)
    @RequiredPermission(SecurityLevel.Employee)
    public Result GetOutTicketInfo(HttpServletRequest request){
        String ticketCode=request.getParameter("ticketCode");
        String storeCode=request.getParameter("storeCode");
        if (StringUtils.isEmpty(ticketCode)||StringUtils.isEmpty(storeCode)) return this.GenerateResult(ResultType.ParametersNeeded);
        Result result=new Result();
        StorageOutTicket ticket=outTicketService.GetTicketInfos(ticketCode,storeCode);
        if(ticket==null){
            result.setResultStatus(-1);
            result.setMessage("no out bound ticket");
        }else {
            result.setData(ticket);
        }
        return result;
    }
}
