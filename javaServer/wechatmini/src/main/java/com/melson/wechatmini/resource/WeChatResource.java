package com.melson.wechatmini.resource;

import com.melson.base.BaseResource;
import com.melson.base.Result;
import com.melson.base.ResultType;
import com.melson.base.cache.CacheKey;
import com.melson.base.cache.CacheUtil;
import com.melson.base.interceptor.RequiredPermission;
import com.melson.base.interceptor.SecurityLevel;
import com.melson.base.utils.JsonToObjectUtil;
import com.melson.webserver.Vo.OutBoundVo;
import com.melson.webserver.Vo.WechatOutBoundVo;
import com.melson.webserver.entity.ProductStorage;
import com.melson.webserver.service.IProductStorage;
import com.melson.webserver.service.IStorageOutTicket;
import com.melson.wechatmini.entity.WeChatApi;
import com.melson.wechatmini.entity.WeChatAppInfo;
import com.melson.wechatmini.entity.WeChatUser;
import com.melson.wechatmini.service.IWeChatUser;
import com.melson.wechatmini.vo.WeChatLoginVo;
import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/3
 */
@RestController
@RequestMapping(value = "/weChat")
public class WeChatResource extends BaseResource {
    private final IProductStorage productStorageService;
    private final CacheUtil cacheUtil;
    private final IWeChatUser weChatUserService;
    private final IStorageOutTicket outTicketService;

    public WeChatResource(IProductStorage productStorageService, CacheUtil cacheUtil, IWeChatUser weChatUserService, IStorageOutTicket outTicketService) {
        this.productStorageService = productStorageService;
        this.cacheUtil = cacheUtil;
        this.weChatUserService = weChatUserService;
        this.outTicketService = outTicketService;
    }

    @RequestMapping(value = "/productStorage",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result findProductStorage(@RequestBody JSONObject param, HttpServletRequest request) {
        String prodName = param.getString("productName");
        String storeCode = param.getString("storeCode");
        if (StringUtils.isEmpty(prodName) || StringUtils.isEmpty(storeCode))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<ProductStorage> storageList = productStorageService.findStorageAndBatchByPName(prodName, storeCode);
        if (storageList == null) {
            result.setResultStatus(2);
            result.setMessage("empty result");
        } else {
            result.setData(storageList);
        }
        System.out.println("Rest Call: /weChat/productStorage ...");
        return result;
    }

    @RequestMapping(value = "/outBoundList",method = RequestMethod.POST)
    @RequiredPermission(SecurityLevel.Employee)
    public Result findOutBoundList(@RequestBody JSONObject param, HttpServletRequest request) {
        String key = param.getString("key");
        String storeCode = param.getString("storeCode");
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(storeCode))
            return this.GenerateResult(ResultType.ParametersNeeded);
        Result result = new Result();
        List<WechatOutBoundVo> voList=outTicketService.findOutBoundListForWechat(key,storeCode);
        if (voList == null) {
            result.setResultStatus(2);
            result.setMessage("empty result");
        } else {
            result.setData(voList);
        }
        System.out.println("Rest Call: /weChat/outBoundList ...");
        return result;
    }


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result weChatLogin(@RequestBody WeChatLoginVo vo){
        WeChatAppInfo appInfo=cacheUtil.GetObjectValue(CacheKey.WeChatAppInfo,WeChatAppInfo.class);
        Map<String, WeChatApi> apiMap=cacheUtil.GetObjectValue(CacheKey.WeChatApi,Map.class);
        WeChatUser user= JsonToObjectUtil.jsonToPojo(vo.getRowData(),WeChatUser.class);
        Result result=new Result();
        WeChatLoginVo loginVo= weChatUserService.WeChatLogin(appInfo,apiMap.get("login"),vo.getCode(),user);
        result.setData(loginVo);
        return result;
    }

    @RequestMapping(value = "/bindUser",method = RequestMethod.POST)
    public Result bindRetailerUser(@RequestBody WeChatLoginVo vo){
        WeChatUser user= JsonToObjectUtil.jsonToPojo(vo.getRowData(),WeChatUser.class);
        return weChatUserService.BindRetailerUser(user);
    }
}