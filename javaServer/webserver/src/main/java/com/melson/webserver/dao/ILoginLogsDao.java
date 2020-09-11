package com.melson.webserver.dao;

import com.melson.webserver.entity.LoginLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Messi on 2020/8/31
 */
@Repository
public interface ILoginLogsDao extends JpaRepository<LoginLogs,String> {
}
