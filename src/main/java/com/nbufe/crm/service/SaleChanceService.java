package com.nbufe.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.SaleChanceMapper;
import com.nbufe.crm.enums.DevResult;
import com.nbufe.crm.enums.StateStatus;
import com.nbufe.crm.query.SaleChanceQuery;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.utils.PhoneUtil;
import com.nbufe.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件查询
     * @param saleChanceQuery
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map=new HashMap<>();

        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //得到对应的营销机会的列表
        List<SaleChance> saleChanceList=saleChanceMapper.selectByParams(saleChanceQuery);
        //得到分页对象
        PageInfo<SaleChance> pageInfo=new PageInfo<>(saleChanceList);

        //LayUI数据表格要求的数据格式
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        System.out.println(map);

        return map;
    }

    /**
     * 营销机会添加
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //参数校验
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设置参数的默认值
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            //未分配
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
            saleChance.setAssignTime(null);
        }else{
            //已分配
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());

        }
        //执行添加操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"营销机会添加失败!");
    }

    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系人电话不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系人电话格式不正确！");
    }

    /**
     * 更新营销机会
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){

        //参数校验
        AssertUtil.isTrue(saleChance.getId()==null,"营销数据更新失败！");
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        //获取更新前数据
        SaleChance temp=saleChanceMapper.selectByPrimaryKey(saleChance.getId());

        //设置参数默认值
        saleChance.setUpdateDate(new Date());
        saleChance.setCreateMan(temp.getCreateMan());
        if(StringUtils.isBlank(temp.getAssignMan())){
            //修改前未指配分配人
            if(StringUtils.isNotBlank(saleChance.getAssignMan())){
                //修改后指配了分配人
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());
                saleChance.setAssignTime(new Date());

            }

        }else{
            //修改前指配了分配人
            if(StringUtils.isBlank(saleChance.getAssignMan())){
                //修改后未指配分配人
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
                saleChance.setAssignTime(null);
            }else{
                //修改后指配了分配人
                if(!saleChance.getAssignMan().equals(temp.getAssignMan())){
                    saleChance.setAssignTime(new Date());
                }else{
                    saleChance.setAssignTime(temp.getAssignTime());
                }

            }

        }
        //更新营销机会，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"营销机会添加失败！");
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        return saleChanceMapper.queryAllSales();
    }

    /**
     * 删除营销机会
     * @param ids
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        AssertUtil.isTrue(null==ids||ids.length<1,"待删除记录不存在！");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)!= ids.length,"营销机会删除失败！");



    }


    /**
     * 更新营销机会开发状态
     * @param saleChanceId
     * @param devResult
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer saleChanceId, Integer devResult) {
        //判断id是否为空
        AssertUtil.isTrue(null==saleChanceId||null==devResult,"系统异常，更新失败！");
        SaleChance saleChance=saleChanceMapper.selectByPrimaryKey(saleChanceId);
        AssertUtil.isTrue(null==saleChance,"待更新记录不存在！");

        saleChance.setDevResult(devResult);
        saleChance.setUpdateDate(new Date());

        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"更新失败！");
    }
}
