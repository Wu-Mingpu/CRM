package com.nbufe.crm.dao;

import com.nbufe.crm.base.BaseMapper;
import com.nbufe.crm.vo.CustomerOrder;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {


    //通过客户id查询客户最后一条订单记录
    CustomerOrder queryLossCustomerOrderByCustomerId(Integer id);
}