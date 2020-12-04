package com.melson.wechatmini.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.melson.base.AbstractService;
import com.melson.base.Result;
import com.melson.base.dao.IStoreEmployeeDao;
import com.melson.base.entity.Store;
import com.melson.base.entity.StoreEmployee;
import com.melson.base.service.IStore;
import com.melson.base.utils.HttpClientUtils;
import com.melson.base.utils.MD5Util;
import com.melson.wechatmini.dao.IWeChatUserDao;
import com.melson.wechatmini.entity.WeChatApi;
import com.melson.wechatmini.entity.WeChatAppInfo;
import com.melson.wechatmini.entity.WeChatUser;
import com.melson.wechatmini.service.IWeChatUser;
import com.melson.wechatmini.vo.WeChatLoginVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/4
 */
@Service
public class WeChatUserImpl extends AbstractService<WeChatUser> implements IWeChatUser {
    private final IWeChatUserDao weChatUserDao;
    private final IStoreEmployeeDao storeEmployeeDao;
    private final IStore storeService;

    public WeChatUserImpl(IWeChatUserDao weChatUserDao, IStoreEmployeeDao storeEmployeeDao, IStore storeService) {
        this.weChatUserDao = weChatUserDao;
        this.storeEmployeeDao = storeEmployeeDao;
        this.storeService = storeService;
    }

    @Override
    public JpaRepository<WeChatUser, String> getRepository() {
        return weChatUserDao;
    }

    @Override
    public WeChatLoginVo WeChatLogin(WeChatAppInfo appInfo, WeChatApi api, String code, WeChatUser user) {
        String apiUrl = api.getApiUrl();
        String[] replayments = api.getApiReplacements().split(api.getSplitKey());
        for (String replace : replayments) {
            if (StringUtils.isEmpty(replace)) continue;
            if (replace.contains("APPID")) {
                apiUrl = apiUrl.replace(replace, appInfo.getAppId());
            }
            if (replace.contains("SECRET")) {
                apiUrl = apiUrl.replace(replace, appInfo.getAppSecret());
            }
            if (replace.contains("JSCODE")) {
                apiUrl = apiUrl.replace(replace, code);
            }
        }
        JSONObject jsonObject = HttpClientUtils.doGet(apiUrl);
        String openId = jsonObject.getString("openid");
        String sessionKey = jsonObject.getString("session_key");
        WeChatUser existUser = weChatUserDao.findByOpenId(openId);
        //系统不存在用户则为新用户，保存当前用户信息
        WeChatUser saved;
        if (existUser == null) {
            user.setLoginDate(new Date());
            user.setOpenId(openId);
            user.setSessionKey(sessionKey);
            saved = weChatUserDao.save(user);
        } else {
            //用户存在 刷新sessionKey 保存
            existUser.setSessionKey(sessionKey);
            existUser.setLoginDate(new Date());
            saved = weChatUserDao.save(existUser);
        }
        WeChatLoginVo vo = new WeChatLoginVo();
        vo.setStoreCode(saved.getStoreCode());
        vo.setRetailerUserId(saved.getRetailerUserId());
        vo.setStoreName(saved.getStoreName());
        vo.setLoginName(saved.getLoginName());
        //返回当前用户至前台，当缺少retailerUserId 时 则未绑定系统账户
        return vo;
    }

    @Override
    public Result BindRetailerUser(WeChatUser user) {
        Result result=new Result();
        if(user==null||StringUtils.isEmpty(user.getNickName())){
            result.setResultStatus(2);
            result.setMessage("Empty WeChat User, please login");
            return result;
        }
        WeChatUser existUser=weChatUserDao.findByNickName(user.getNickName());
        if(existUser==null){
            result.setResultStatus(2);
            result.setMessage("please login first");
            return result;
        }
        String md5Pass = MD5Util.string2MD5(user.getPassword());
        StoreEmployee existEmployee = storeEmployeeDao.findByLoginNameAndPassword(user.getLoginName(), md5Pass);
        if(existEmployee==null){
            result.setResultStatus(3);
            result.setMessage("wrong loginName or Password");
            return result;
        }
        Store store=storeService.findByCode(existEmployee.getStoreCode());
        existUser.setStoreName(store.getStoreName());
        existUser.setStoreCode(existEmployee.getStoreCode());
        existUser.setLoginName(existEmployee.getLoginName());
        existUser.setPassword(existEmployee.getPassword());
        existUser.setRetailerUserId(existEmployee.getUserId());
        WeChatUser saved= weChatUserDao.save(existUser);
        WeChatLoginVo vo = new WeChatLoginVo();
        vo.setStoreCode(saved.getStoreCode());
        vo.setRetailerUserId(saved.getRetailerUserId());
        vo.setStoreName(saved.getStoreName());
        vo.setLoginName(saved.getLoginName());
        result.setData(vo);
        return result;
    }
}
