package com.melson.webserver.Vo;

/**
 * @Author Nelson
 * @Description excel 导出结果封装类
 * @Date 2020/10/21
 */
public class ExcelExportResultVo {
    //1 为成功 其他失败
    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
