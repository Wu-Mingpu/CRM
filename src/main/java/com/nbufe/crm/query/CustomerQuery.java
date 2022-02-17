package com.nbufe.crm.query;

import com.nbufe.crm.base.BaseQuery;

public class CustomerQuery extends BaseQuery {
    private String cusName;//客户名
    private String cusNo;//客户编号
    private String cusLevel;//客户级别

    private String time; // 订单时间
    private Integer type; // 金额区间  1=1-1000 2=1000-3000  3=3000-5000  4=5000以上

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusNo() {
        return cusNo;
    }

    public void setCusNo(String cusNo) {
        this.cusNo = cusNo;
    }

    public String getCusLevel() {
        return cusLevel;
    }

    public void setCusLevel(String cusLevel) {
        this.cusLevel = cusLevel;
    }
}
