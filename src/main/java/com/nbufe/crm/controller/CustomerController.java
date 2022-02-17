package com.nbufe.crm.controller;

import com.nbufe.crm.annotation.RequiredPermission;
import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.service.CustomerLinkmanService;
import com.nbufe.crm.service.CustomerService;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.query.CustomerQuery;
import com.nbufe.crm.vo.Customer;
import com.nbufe.crm.vo.CustomerLinkman;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    @Resource
    private CustomerLinkmanService customerLinkmanService;

    /**
     * 多条件查询客户列表
     * @param customerQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerList(CustomerQuery customerQuery){
        return customerService.queryByParamsForTable(customerQuery);
    }

    /**
     * 到客户管理界面
     * @return
     */
    @RequestMapping("index")
    public String toCustomerPage(){
        return "customer/customer";
    }

    /**
     * 客户添加
     * @param customer
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success();


    }

    /**
     * 客户编辑
     * @param customer
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return success();


    }

    /**
     * 到添加更新用户界面
     * @return
     */
    @RequestMapping("toAddUpdateCustomerPage")
    public String toAddUpdateCustomerPage(Integer customerId, HttpServletRequest request){
        if(customerId!=null){
            Customer customer=customerService.selectByPrimaryKey(customerId);
            request.setAttribute("customer",customer);
        }
        return "customer/add_update";
    }

    /**
     * 删除营销机会
     * @param customerId
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer customerId){
        customerService.deleteCustomer(customerId);
        return success();
    }

    /**
     * 到订单查看界面
     * @return
     */

    @RequestMapping("toOrderInfoPage")
    public String toOrderInfoPage(Integer cId,HttpServletRequest request){
        Customer customer=customerService.selectByPrimaryKey(cId);
        request.setAttribute("customer",customer);

        return "customer/customer_order";
    }

    /**
     * 到联系人管理界面
     * @return
     */
    @RequestMapping("toLinkmanPage")
    public String toLinkmanPage(Integer cusId,HttpServletRequest request){
        CustomerLinkman customerLinkman=customerLinkmanService.queryCustomerLinkmanByCusId(cusId);
        request.setAttribute("customerLinkman",customerLinkman);
        return "customer/customer_linkman";
    }

    /**
     * 到客户交往记录界面
     * @return
     */
    @RequestMapping("toCustomerContactPage")
    public String toCustomerContactPage(Integer cusId,HttpServletRequest request){
        request.setAttribute("cusId",cusId);
        System.out.println(cusId);
        return "customer/customer_contact";
    }

    /**
     * 客户贡献分析
     * @param customerQuery
     * @return
     */
    @RequestMapping("queryCustomerContributionByParams")
    @ResponseBody
    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerContributionByParams(customerQuery);
    }

    /**
     * 查询客户构成 （折线图）
     * @return
     */
    @RequestMapping("countCustomerMake")
    @ResponseBody
    public Map<String, Object> countCustomerMake() {
        return customerService.countCustomerMake();
    }


    /**
     * 查询客户构成 （饼状图）
     * @return
     */
    @RequestMapping("countCustomerMake02")
    @ResponseBody
    public Map<String, Object> countCustomerMake02() {
        return customerService.countCustomerMake02();
    }


    /**
     * 查询客户服务 （折线图）
     * @return
     */
    @RequestMapping("countCustomerServe")
    @ResponseBody
    public Map<String,Object> countCustomerServe(){
        return customerService.countCustomerServe();
    }


    /**
     * 查询客户服务（饼状图）
     * @return
     */
    @RequestMapping("countCustomerServe02")
    @ResponseBody
    public Map<String,Object> countCustomerServe02() {
        return customerService.countCustomerServe02();
    }



}
