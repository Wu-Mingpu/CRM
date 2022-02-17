package com.nbufe.crm.query;

import com.nbufe.crm.base.BaseQuery;

public class CustomerLossQuery extends BaseQuery {
    private String cusNo;//客户编号
    private String cusName;//客户名称
    private Integer state;//流失状态 0-暂缓流失 1-确认流失

    public String getCusNo() {
        return cusNo;
    }

    public void setCusNo(String cusNo) {
        this.cusNo = cusNo;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
