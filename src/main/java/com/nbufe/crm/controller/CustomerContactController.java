package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.query.CustomerContactQuery;
import com.nbufe.crm.query.CustomerOrderQuery;
import com.nbufe.crm.service.CustomerContactService;
import com.nbufe.crm.vo.CustomerContact;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer_contact")
public class CustomerContactController extends BaseController {

    @Resource
    private CustomerContactService customerContactService;

    /**
     * 客户交往记录列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerContactList(CustomerContactQuery customerContactQuery){
        return customerContactService.queryByParamsForTable(customerContactQuery);

    }

    /**
     * 到添加更新客户交往记录界面
     * @param contactId
     * @param request
     * @return
     */
    @RequestMapping("toAddUpdateContactPage")
    public String toAddUpdateContactPage(Integer cusId,Integer contactId, HttpServletRequest request){
        CustomerContact customerContact=customerContactService.selectByPrimaryKey(contactId);
        request.setAttribute("customerContact",customerContact);
        request.setAttribute("cusId",cusId);
        return "customer/contact_add_update";


    }

    /**
     * 添加客户交往记录
     * @param customerContact
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addContact(CustomerContact customerContact){
        customerContactService.addContact(customerContact);
        return success();

    }

    /**
     * 更新客户交往记录
     * @param customerContact
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateContact(CustomerContact customerContact){
        customerContactService.updateContact(customerContact);
        return success();

    }

    /**
     * 删除客户交往记录
     * @param contactId
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteContact(Integer contactId){
        customerContactService.deleteContact(contactId);
        return success();

    }
}
