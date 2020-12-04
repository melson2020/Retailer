package com.melson.wechatmini.service;

import com.melson.base.IService;
import com.melson.base.Result;
import com.melson.wechatmini.entity.WeChatApi;
import com.melson.wechatmini.entity.WeChatAppInfo;
import com.melson.wechatmini.entity.WeChatUser;
import com.melson.wechatmini.vo.WeChatLoginVo;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/4
 */
public interface IWeChatUser extends IService<WeChatUser> {
    WeChatLoginVo WeChatLogin(WeChatAppInfo appInfo, WeChatApi api, String code, WeChatUser user);
    Result BindRetailerUser(WeChatUser user);
}
