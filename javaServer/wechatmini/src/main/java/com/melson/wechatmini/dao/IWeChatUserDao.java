package com.melson.wechatmini.dao;

import com.melson.wechatmini.entity.WeChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/4
 */
@Repository
public interface IWeChatUserDao extends JpaRepository<WeChatUser,String> {
    WeChatUser findByOpenId(String openId);
    WeChatUser findByNickName(String nickName);
}
