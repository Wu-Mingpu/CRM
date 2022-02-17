package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.query.CusDevPlanQuery;
import com.nbufe.crm.service.CusDevPlanService;
import com.nbufe.crm.service.SaleChanceService;
import com.nbufe.crm.vo.CusDevPlan;
import com.nbufe.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;

    /**
     * 进入客户开发计划界面
     * @return
     */
    @RequestMapping("index")
    public String toCusDevPlanPage(){
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 弹出计划项数据对话框
     * @return
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDialog(HttpServletRequest request,Integer sId){
        SaleChance saleChance=saleChanceService.selectByPrimaryKey(sId);
        request.setAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     *查询营销机会管理客户开发数据项列表
     * @param cusDevPlanQuery
     * @return
     */

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryByParamsForTable(cusDevPlanQuery);

    }

    /**
     * 添加计划项数据
     * @param cusDevPlan
     * @return
     */

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success();

    }

    /**
     * 更新计划项数据
     * @param cusDevPlan
     * @return
     */

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success();

    }


    /**
     * 进入添加更新客户营销管理计划项界面
     * @param id
     * @param request
     * @param sId 前台传回的营销机会id
     * @return
     */

    @RequestMapping("toAddUpdateCusDevPlanPage")
    public String toAddUpdateCusDevPlanPage(Integer id,HttpServletRequest request,Integer sId){
        //将客户开发计划项列表界面获取的营销机会id设置作用域，通过隐藏域传给添加更新界面
        request.setAttribute("sId",sId);
        if(id!=null){
            CusDevPlan cusDevPlan=cusDevPlanService.selectByPrimaryKey(id);
            request.setAttribute("cusDevPlan",cusDevPlan);
        }
        return "cusDevPlan/add_update";
    }

    /**
     * 删除计划项
     * @param id
     * @return
     */

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success();
    }
}
