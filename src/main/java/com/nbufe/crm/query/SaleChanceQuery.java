package com.nbufe.crm.query;

import com.nbufe.crm.base.BaseQuery;

/**
 * 条件查询对象
 */
public class SaleChanceQuery extends BaseQuery {
    private String customerName;//客户名字
    private String createMan;//创建人
    private String state;//分配状态

    private String assignMan;//指派人的id
    private String devResult;//开发状态

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public String getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(String assignMan) {
        this.assignMan = assignMan;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
