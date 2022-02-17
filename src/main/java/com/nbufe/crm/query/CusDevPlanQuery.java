package com.nbufe.crm.query;

import com.nbufe.crm.base.BaseQuery;
import com.nbufe.crm.base.BaseService;

public class CusDevPlanQuery extends BaseQuery {
    private Integer sId;//营销机会ID

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }
}
