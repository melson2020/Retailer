package com.melson.wechatmini.dao;

import com.melson.wechatmini.entity.WeChatAppInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/4
 */
@Repository
public interface IWeChatAppInfoDao extends JpaRepository<WeChatAppInfo,String> {
    WeChatAppInfo findByAppName(String name);
}
