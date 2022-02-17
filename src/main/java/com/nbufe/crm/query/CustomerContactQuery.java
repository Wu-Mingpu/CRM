package com.nbufe.crm.query;

import com.nbufe.crm.base.BaseQuery;

public class CustomerContactQuery extends BaseQuery {
    private Integer cusId;//客户id

    public Integer getCusId() {
        return cusId;
    }

    public void setCusId(Integer cusId) {
        this.cusId = cusId;
    }
}
