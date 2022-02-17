package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.CustomerLossMapper;
import com.nbufe.crm.dao.CustomerMapper;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.vo.CustomerLoss;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {

    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 更新流失客户的流失状态
     * @param id
     * @param lossReason
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerLossStateById(Integer id, String lossReason) {
        AssertUtil.isTrue(id==null,"待确认流失的客户不存在！");
        CustomerLoss customerLoss=customerLossMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null==customerLoss,"待确认流失的客户不存在!");
        AssertUtil.isTrue(null==lossReason,"流失原因不能为空!");

        customerLoss.setState(1);
        customerLoss.setLossReason(lossReason);
        customerLoss.setConfirmLossTime(new Date());
        customerLoss.setUpdateDate(new Date());

        AssertUtil.isTrue(customerLossMapper.updateByPrimaryKeySelective(customerLoss)<1,"确认流失失败！");
    }
}
