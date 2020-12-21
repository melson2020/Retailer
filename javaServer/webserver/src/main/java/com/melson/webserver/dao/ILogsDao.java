package com.melson.webserver.dao;

import com.melson.webserver.entity.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Messi on 2020/8/31
 */
@Repository
public interface ILogsDao extends JpaRepository<Logs,String> {
    @Query(nativeQuery = true,value = "Select * from loginlogs where storeCode=?1")
    Logs findWithQuery(String log);
}
