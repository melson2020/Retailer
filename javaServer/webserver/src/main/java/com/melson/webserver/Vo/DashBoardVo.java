package com.melson.webserver.Vo;

import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/12/23
 */
public class DashBoardVo {
   List<DashBoardItemVo> productList;
   List<DashBoardItemVo> employeeList;
   List<DashBoardItemVo> sortList;

    public List<DashBoardItemVo> getProductList() {
        return productList;
    }

    public void setProductList(List<DashBoardItemVo> productList) {
        this.productList = productList;
    }

    public List<DashBoardItemVo> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<DashBoardItemVo> employeeList) {
        this.employeeList = employeeList;
    }

    public List<DashBoardItemVo> getSortList() {
        return sortList;
    }

    public void setSortList(List<DashBoardItemVo> sortList) {
        this.sortList = sortList;
    }
}
