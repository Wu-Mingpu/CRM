package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.query.CustomerReprieveQuery;
import com.nbufe.crm.service.CustomerReprieveService;
import com.nbufe.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_reprieve")
public class CustomerReprieveController extends BaseController {

    @Resource
    private CustomerReprieveService customerReprieveService;

    /**
     * 查询流失客户暂缓列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerReprieveList(CustomerReprieveQuery customerReprieveQuery){
        return customerReprieveService.queryByParamsForTable(customerReprieveQuery);


    }

    /**
     * 添加暂缓
     * @param customerReprieve
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addReprieve(CustomerReprieve customerReprieve){

        customerReprieveService.addReprieve(customerReprieve);
        return success();
    }

    /**
     * 更新暂缓
     * @param customerReprieve
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateReprieve(CustomerReprieve customerReprieve){
        customerReprieveService.updateReprieve(customerReprieve);
        return success();
    }

    /**
     * 删除暂缓
     * @param repId
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteReprieve(Integer repId){
        customerReprieveService.deleteReprieve(repId);
        return success();
    }
}
