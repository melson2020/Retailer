package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.webserver.entity.StorageInTicket;
import com.melson.webserver.service.IStorageInTicket;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
@RestController
@RequestMapping(value = "/storageIn")
public class StorageInResource extends BaseResource {
    private final IStorageInTicket storageInTicketService;

    public StorageInResource(IStorageInTicket storageInTicketService) {
        this.storageInTicketService = storageInTicketService;
    }

    @RequestMapping(value = "/saveTicket",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result SaveStorageInTicket(@RequestBody StorageInTicket ticket){
        if(StringUtils.isEmpty(ticket.getStoreCode()))return this.GenerateResult(ResultType.ParametersNeeded);
        Result result=new Result();
        Integer res= storageInTicketService.SaveStorageInTicket(ticket);
        result.setResultStatus(res);
        result.setMessage(res==1?"":res==-1?"update storage fail":"save record fail");
        return result;
    }
}
