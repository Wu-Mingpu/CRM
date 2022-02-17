package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.CustomerMapper;
import com.nbufe.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {

    @Resource
    private CustomerMapper customerMapper;
}
