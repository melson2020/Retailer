package com.melson.base.utils;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
public class EntityManagerExcuteRs {
    private Integer status;
    private String message;
    private List<Integer> falseIndexs;

    public List<Integer> getFalseIndexs() {
        return falseIndexs;
    }

    public void setFalseIndexs(List<Integer> falseIndexs) {
        this.falseIndexs = falseIndexs;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
