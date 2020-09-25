package com.melson.webserver.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.webserver.service.IStorageOutTicket;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

}
