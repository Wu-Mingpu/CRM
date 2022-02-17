package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.CustomerMapper;
import com.nbufe.crm.dao.CustomerServeMapper;
import com.nbufe.crm.dao.UserMapper;
import com.nbufe.crm.enums.CustomerServeStatus;
import com.nbufe.crm.query.CustomerServeQuery;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.vo.CustomerServe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class CustomerServeService extends BaseService<CustomerServe,Integer> {

    @Resource
    private CustomerServeMapper customerServeMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private UserMapper userMapper;


    /**
     * 添加客户服务
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void adddServe(CustomerServe customerServe) {
        /*1.参数校验*/
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getCustomer()),"客户名不能为空!");
        AssertUtil.isTrue(null==customerMapper.selectCustomerByName(customerServe.getCustomer()),"客户不存在！");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServeType()),"服务类型不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceRequest()),"服务内容不能为空！");

        /*2.设置默认值*/
        customerServe.setState(customerServe.getState());
        customerServe.setIsValid(1);
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());

        /*3.执行添加操作*/
        AssertUtil.isTrue(customerServeMapper.insertSelective(customerServe)<1,"添加服务失败！");

    }

    /**
     * 服务分配、服务处理、服务反馈
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUpdateCustomerServe(CustomerServe customerServe) {
        //参数校验
        AssertUtil.isTrue(null==customerServe.getId()||null==customerServeMapper.selectByPrimaryKey(customerServe.getId()),"待更新服务记录不存在！");

        if(CustomerServeStatus.ASSIGNED.getState().equals(customerServe.getState())){
            /*服务分配*/
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getAssigner()),"待分配用户不能为空！");
            AssertUtil.isTrue(null==userMapper.selectByPrimaryKey(Integer.parseInt(customerServe.getAssigner())),"待分配用户不存在！");

            customerServe.setAssignTime(new Date());

        }else if (CustomerServeStatus.PROCED.getState().equals(customerServe.getState())){
            /*服务处理*/
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()), "服务处理内容不能为空！");
            customerServe.setServiceProceTime(new Date());

        }else if(CustomerServeStatus.FEED_BACK.getState().equals(customerServe.getState())){
            /*服务反馈*/
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()), "服务反馈内容不能为空！");
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getMyd()), "请选择服务反馈满意度！");
            // 服务状态      设置为 服务归档状态 fw_005
            customerServe.setState(CustomerServeStatus.ARCHIVED.getState());

        }

        //设置默认值
        customerServe.setUpdateDate(new Date());


        //执行操作
        AssertUtil.isTrue(customerServeMapper.updateByPrimaryKeySelective(customerServe)<1,"服务更新失败！");
    }
}
