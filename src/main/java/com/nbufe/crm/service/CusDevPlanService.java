package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.CusDevPlanMapper;
import com.nbufe.crm.dao.SaleChanceMapper;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.vo.CusDevPlan;
import com.nbufe.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 添加计划项数据
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        //参数校验
        checkCusDevPlanParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        //设置默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());

        //执行添加操作
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)<1,"计划项数据添加失败！");

    }

    /**
     * 参数校验
     * @param saleChanceId
     * @param planItem
     * @param planDate
     */
    private void checkCusDevPlanParams(Integer saleChanceId, String planItem, Date planDate) {
        AssertUtil.isTrue(null==saleChanceId,"系统异常，添加失败！");
        AssertUtil.isTrue(null==saleChanceMapper.selectByPrimaryKey(saleChanceId),"营销机会数据不存在！");
        AssertUtil.isTrue(StringUtils.isBlank(planItem),"计划项内容不能为空！");
        AssertUtil.isTrue(null==planDate,"计划项时间不能为空！");
    }

    /**
     * 更新计划项数据
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        //参数校验
        AssertUtil.isTrue(null==cusDevPlan.getId(),"系统异常，更新失败！");
        checkCusDevPlanParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        //设置默认值
        cusDevPlan.setUpdateDate(new Date());

        //执行添加操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"计划项数据更新失败！");


    }


    /**
     * 删除计划项
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id){
        AssertUtil.isTrue(null==id,"系统异常，删除失败！");
        CusDevPlan cusDevPlan=cusDevPlanMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null==cusDevPlan,"待删除记录不存在！");

        cusDevPlan.setUpdateDate(new Date());
        cusDevPlan.setIsValid(0);

        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"计划项删除失败！");

    }

}
