package com.melson.webserver.dao;

import com.melson.webserver.entity.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/4
 */
@Repository
public interface ISysConfigDao extends JpaRepository<SysConfig,String> {
}
