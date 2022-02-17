package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.OrderDetailsMapper;
import com.nbufe.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {

    @Resource
    private OrderDetailsMapper orderDetailsMapper;
}
