package com.melson.webserver.Vo;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2021/1/4
 */
public class OutBoundDeliveryVo {
    private String startDate;
    private String endDate;
    private List<OutBoundVo> outBoundVoList;

    public OutBoundDeliveryVo(String startDate, String endDate, List<OutBoundVo> outBoundVoList) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.outBoundVoList = outBoundVoList;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<OutBoundVo> getOutBoundVoList() {
        return outBoundVoList;
    }

    public void setOutBoundVoList(List<OutBoundVo> outBoundVoList) {
        this.outBoundVoList = outBoundVoList;
    }
}
