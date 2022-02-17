package com.nbufe.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.CustomerContactMapper;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.vo.CustomerContact;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CustomerContactService extends BaseService<CustomerContact,Integer> {

    @Resource
    private CustomerContactMapper customerContactMapper;

    /**
     * 添加客户交往记录
     * @param customerContact
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addContact(CustomerContact customerContact) {
        checkContactParams(customerContact.getContactTime(),customerContact.getAddress());

        customerContact.setIsValid(1);
        customerContact.setCreateDate(new Date());
        customerContact.setUpdateDate(new Date());

        AssertUtil.isTrue(customerContactMapper.insertSelective(customerContact)<1,"交往记录添加失败！");
    }

    /**
     * 交往记录数据校验
     * @param contactTime
     * @param address
     */
    private void checkContactParams(Date contactTime, String address) {
        AssertUtil.isTrue(null==contactTime,"交往时间不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(address),"交往地址不能为空！");

    }

    /**
     * 更新客户交往记录
     * @param customerContact
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateContact(CustomerContact customerContact) {
        AssertUtil.isTrue(null==customerContact.getId()||null==customerContactMapper.selectByPrimaryKey(customerContact.getId()),"待更新记录不存在！");
        checkContactParams(customerContact.getContactTime(),customerContact.getAddress());

        customerContact.setUpdateDate(new Date());

        AssertUtil.isTrue(customerContactMapper.updateByPrimaryKeySelective(customerContact)<1,"交往记录更新失败！");
    }

    /**
     * 删除客户交往记录
     * @param contactId
     */
    public void deleteContact(Integer contactId) {
        CustomerContact customerContact=customerContactMapper.selectByPrimaryKey(contactId);
        AssertUtil.isTrue(null==contactId||null==customerContact,"待删除记录不存在！");

        customerContact.setUpdateDate(new Date());
        customerContact.setIsValid(0);

        AssertUtil.isTrue(customerContactMapper.updateByPrimaryKeySelective(customerContact)<1,"交往记录删除失败！");


    }
}
