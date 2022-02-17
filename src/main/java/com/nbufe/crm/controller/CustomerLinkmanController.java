package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.service.CustomerLinkmanService;
import com.nbufe.crm.vo.CustomerLinkman;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("customer_linkman")
public class CustomerLinkmanController extends BaseController {

    @Resource
    private CustomerLinkmanService customerLinkmanService;

    /**
     * 更新客户联系人信息
     * @return
     */
    @RequestMapping("updateCustomerLinkman")
    @ResponseBody
    public ResultInfo updateCustomerLinkman(CustomerLinkman customerLinkman){
        customerLinkmanService.updateCustomerLinkman(customerLinkman);
        return success();

    }



}
