package com.nbufe.crm.query;

import com.nbufe.crm.base.BaseQuery;

public class OrderDetailsQuery extends BaseQuery {
    private Integer orderId;//客户订单id

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
