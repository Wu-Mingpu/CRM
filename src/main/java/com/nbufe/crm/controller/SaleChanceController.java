package com.nbufe.crm.controller;

import com.nbufe.crm.annotation.RequiredPermission;
import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.query.SaleChanceQuery;
import com.nbufe.crm.service.SaleChanceService;
import com.nbufe.crm.utils.CookieUtil;
import com.nbufe.crm.utils.LoginUserUtil;
import com.nbufe.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 多条件查询
     * @param saleChanceQuery
     * @param flag 1为客户开发计划
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    @RequiredPermission(code="101001")
    public Map<String,Object> querySaleChanceList(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request){

        if(flag!=null&&flag==1) {
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId+"");
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 跳转至营销机会管理界面
     * @return
     */
    @RequestMapping("index")
    @RequiredPermission(code = "1010")
    public String toSaleChancePage(){


        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会
     * @param request
     * @param saleChance
     * @return
     */

    @PostMapping("add")
    @ResponseBody
    @RequiredPermission(code="101002")
    public ResultInfo addSaleChance(HttpServletRequest request,SaleChance saleChance){
        ResultInfo resultInfo=new ResultInfo();

        saleChance.setCreateMan(CookieUtil.getCookieValue(request,"trueName"));
        saleChanceService.addSaleChance(saleChance);
        return resultInfo;
    }

    /**
     * 跳转至营销机会添加更新界面
     * @return
     */

    @RequestMapping("toAddUpdateSaleChancePage")
    public String toAddUpdateSaleChancePage(Integer saleChanceId,HttpServletRequest request){
        if(saleChanceId!=null){
            SaleChance saleChance=saleChanceService.selectByPrimaryKey(saleChanceId);
            request.setAttribute("saleChance",saleChance);

        }
        return "saleChance/add_update";

    }


    /**
     * 更新营销机会
     * @param saleChance
     * @return
     */

    @PostMapping("update")
    @ResponseBody
    @RequiredPermission(code="101004")
    public ResultInfo updateSaleChance(SaleChance saleChance){
        ResultInfo resultInfo=new ResultInfo();


        saleChanceService.updateSaleChance(saleChance);
        return resultInfo;
    }

    /**
     * 查询所有的销售人员
     * @return
     */

    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return saleChanceService.queryAllSales();
    }

    /**
     * 删除营销机会
     * @param ids
     */
    @PostMapping("delete")
    @ResponseBody
    @RequiredPermission(code="101003")
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success();
    }

    /**
     * 更新营销机会的状态
     * @param saleChanceId
     * @param devResult
     * @return
     */

    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer saleChanceId,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(saleChanceId,devResult);
        return success();
    }
}
