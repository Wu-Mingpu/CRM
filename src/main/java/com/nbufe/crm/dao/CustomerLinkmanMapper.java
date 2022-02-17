package com.nbufe.crm.dao;

import com.nbufe.crm.base.BaseMapper;
import com.nbufe.crm.vo.CustomerLinkman;

public interface CustomerLinkmanMapper extends BaseMapper<CustomerLinkman,Integer> {

    /**
     * 通过客户id查询客户联系人对象
     * @param cusId
     * @return
     */
    CustomerLinkman queryCustomerLinkmanByCusId(Integer cusId);
}