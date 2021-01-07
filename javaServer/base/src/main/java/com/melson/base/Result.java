package com.melson.base;

/**
 * Created by Nelson on 2020/6/17.
 */
public class Result {
    private Object data;
    private Integer resultStatus;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result(Object data) {
        this.data = data;
        resultStatus=1;
    }
    public Result() {
        resultStatus=1;
    }

    public Result(Object data,Integer resultStatus) {
        this.data = data;
        this.resultStatus=resultStatus;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(Integer resultStatus) {
        this.resultStatus = resultStatus;
    }
}
