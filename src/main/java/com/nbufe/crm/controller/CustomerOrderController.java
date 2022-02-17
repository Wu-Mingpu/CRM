package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.query.CustomerOrderQuery;
import com.nbufe.crm.service.CustomerOrderService;
import com.nbufe.crm.vo.CustomerOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("order")
public class CustomerOrderController extends BaseController {

    @Resource
    private CustomerOrderService customerOrderService;

    /**
     * 客户订单列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerOrderList(CustomerOrderQuery customerOrderQuery){
        return customerOrderService.queryByParamsForTable(customerOrderQuery);

    }

    /**
     * 进入订单详情页面
     * @return
     */
    @RequestMapping("toOrderDetailPage")
    public String toOrderDetailPage(Integer orderId, HttpServletRequest request){
        if(orderId!=null){
            CustomerOrder order=customerOrderService.selectByPrimaryKey(orderId);
            request.setAttribute("order",order);
        }
        return "customer/customer_order_detail";
    }
}
