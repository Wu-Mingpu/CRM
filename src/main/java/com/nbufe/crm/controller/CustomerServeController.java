package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.query.CustomerServeQuery;
import com.nbufe.crm.service.CustomerServeService;
import com.nbufe.crm.utils.LoginUserUtil;
import com.nbufe.crm.vo.Customer;
import com.nbufe.crm.vo.CustomerServe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer_serve")
public class CustomerServeController extends BaseController {

    @Resource
    private CustomerServeService customerServeService;

    /**
     * 根据类型进入不同的服务界面
     * @param type
     * @return
     */
    @RequestMapping("index/{type}")
    public String toServePage(@PathVariable Integer type){
        if(type!=null){
            if (type == 1) {
                // 服务创建
                return "customerServe/customer_serve";
            } else if (type == 2) {
                // 服务分配
                return "customerServe/customer_serve_assign";
            } else if (type == 3) {
                // 服务处理
                return "customerServe/customer_serve_proce";
            } else if (type == 4) {
                // 服务反馈
                return "customerServe/customer_serve_feed_back";
            } else if (type == 5) {
                // 服务归档
                return "customerServe/customer_serve_archive";
            } else {
                return "";
            }
        }else{
            return "";
        }

    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery,
                                                         Integer flag, HttpServletRequest request) {

        // 判断是否执行服务处理，如果是则查询分配给当前登录用户的服务记录
        if (flag != null && flag == 1) {
            // 设置查询条件：分配人
            customerServeQuery.setAssigner(LoginUserUtil.releaseUserIdFromCookie(request));
        }
        return customerServeService.queryByParamsForTable(customerServeQuery);
    }

    /**
     * 去添加客户服务界面
     * @return
     */
    @RequestMapping("toAddCustomerServePage")
    public String toAddCustomerServePage(){
        return "customerServe/customer_serve_add";

    }

    /**
     * 添加客户服务
     * @param customerServe
     * @param request
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addServe(CustomerServe customerServe,HttpServletRequest request){
        customerServe.setCreatePeople(LoginUserUtil.releaseUserNameFromCookie(request));
        customerServeService.adddServe(customerServe);
        return success();
    }

    /**
     * 进入服务分配界面
     * @param serveId
     * @param request
     * @return
     */
    @RequestMapping("toCustomerServeAssignPage")
    public String toCustomerServeAssignPage(Integer serveId,HttpServletRequest request){
        CustomerServe customerServe=customerServeService.selectByPrimaryKey(serveId);
        request.setAttribute("customerServe",customerServe);
        return "customerServe/customer_serve_assign_add";
    }

    /**
     * 服务分配、服务处理、服务反馈
     * @param customerServe
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo addUpdateCustomerServe(CustomerServe customerServe){
        customerServeService.addUpdateCustomerServe(customerServe);
        return success();

    }

    /**
     * 去服务处理界面
     * @param serveId
     * @param request
     * @return
     */
    @RequestMapping("toCustomerServeProcePage")
    public String toCustomerServeProcePage(Integer serveId,HttpServletRequest request){
        CustomerServe customerServe=customerServeService.selectByPrimaryKey(serveId);
        request.setAttribute("customerServe",customerServe);

        return "customerServe/customer_serve_proce_add";
    }

    /**
     * 去服务反馈界面
     * @param serveId
     * @param request
     * @return
     */
    @RequestMapping("toCustomerServeFeedbackPage")
    public String toCustomerServeFeedbackPage(Integer serveId,HttpServletRequest request){
        CustomerServe customerServe=customerServeService.selectByPrimaryKey(serveId);
        request.setAttribute("customerServe",customerServe);

        return "customerServe/customer_serve_feed_back_add";
    }
}
