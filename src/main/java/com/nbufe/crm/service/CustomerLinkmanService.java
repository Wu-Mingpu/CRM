package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.CustomerLinkmanMapper;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.utils.PhoneUtil;
import com.nbufe.crm.vo.CustomerLinkman;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CustomerLinkmanService extends BaseService<CustomerLinkman,Integer> {

    @Resource
    private CustomerLinkmanMapper customerLinkmanMapper;

    /**
     * 通过客户id查询客户联系人对象
     * @param cusId
     * @return
     */
    public CustomerLinkman queryCustomerLinkmanByCusId(Integer cusId) {
        return customerLinkmanMapper.queryCustomerLinkmanByCusId(cusId);

    }

    /**
     * 更新客户联系人信息
     */
    public void updateCustomerLinkman(CustomerLinkman customerLinkman) {
        CustomerLinkman temp=customerLinkmanMapper.selectByPrimaryKey(customerLinkman.getId());
        AssertUtil.isTrue(null==customerLinkman.getId()||null==temp,"待更新记录不存在！");
        checkLinkManParams(customerLinkman.getLinkName(),customerLinkman.getPhone(),customerLinkman.getPosition());

        customerLinkman.setUpdateDate(new Date());

        AssertUtil.isTrue(customerLinkmanMapper.updateByPrimaryKeySelective(customerLinkman)<1,"联系人更新失败！");



    }

    /**
     * 客户联系人参数校验
     * @param linkName
     * @param phone
     * @param position
     */
    private void checkLinkManParams(String linkName, String phone, String position) {
        AssertUtil.isTrue(StringUtils.isBlank(linkName),"联系人姓名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空！");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(phone)),"手机号格式不正确！");
        AssertUtil.isTrue(StringUtils.isBlank(position),"职位不能为空！");
    }
}
