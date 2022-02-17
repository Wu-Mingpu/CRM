package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.query.CustomerLossQuery;
import com.nbufe.crm.service.CustomerLossService;
import com.nbufe.crm.service.CustomerReprieveService;
import com.nbufe.crm.vo.CustomerLoss;
import com.nbufe.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer_loss")
public class CustomerLossController extends BaseController {

    @Resource
    private CustomerLossService customerLossService;

    @Resource
    private CustomerReprieveService customerReprieveService;

    /**
     * 进入流失客户管理界面
     * @return
     */
    @RequestMapping("index")
    public String toCustomerLossPage(){
        return "customerLoss/customer_loss";
    }

    /**
     * 查询流失客户列表
     * @param customerLossQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> customerLossList(CustomerLossQuery customerLossQuery){
        return customerLossService.queryByParamsForTable(customerLossQuery);
    }

    /**
     * 进入流失客户暂缓界面
     * @param lossId
     * @return
     */
    @RequestMapping("toCustomerReprievePage")
    public String toCustomerReprievePage(Integer lossId, HttpServletRequest request){
        if(lossId!=null){
            CustomerLoss customerLoss=customerLossService.selectByPrimaryKey(lossId);
            request.setAttribute("customerLoss",customerLoss);

        }
        return "customerLoss/customer_rep";
    }

    /**
     * 到添加跟新暂缓界面
     * @return
     */
    @RequestMapping("toAddUpdateCustomerReprievePage")
    public String toAddUpdateCustomerReprievePage(Integer repId,Integer lossId,HttpServletRequest request){
        if(repId!=null){
            CustomerReprieve customerRep=customerReprieveService.selectByPrimaryKey(repId);
            request.setAttribute("customerRep",customerRep);
        }
        request.setAttribute("lossId",lossId);
        return "customerLoss/customer_rep_add_update";
    }


    /**
     * 更新流失客户的流失状态
     * @param id
     * @param lossReason
     * @return
     */
    @RequestMapping("updateCustomerLossStateById")
    @ResponseBody
    public ResultInfo updateCustomerLossStateById(Integer id,String lossReason){
        customerLossService.updateCustomerLossStateById(id,lossReason);
        return success();


    }

}
