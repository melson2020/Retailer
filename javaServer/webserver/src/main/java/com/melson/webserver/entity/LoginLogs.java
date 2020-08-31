package com.melson.webserver.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Messi on 2020/8/31
 */
@Entity
@Table(name = "loginLogs")
public class LoginLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String logs;
    private Date logDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
}
