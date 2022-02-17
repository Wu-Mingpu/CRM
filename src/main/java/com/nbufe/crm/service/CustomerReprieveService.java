package com.nbufe.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.CustomerLossMapper;
import com.nbufe.crm.dao.CustomerReprieveMapper;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.vo.CusDevPlan;
import com.nbufe.crm.vo.CustomerLoss;
import com.nbufe.crm.vo.CustomerReprieve;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CustomerReprieveService extends BaseService<CustomerReprieve,Integer> {

    @Resource
    private CustomerReprieveMapper customerReprieveMapper;

    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 添加暂缓
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addReprieve(CustomerReprieve customerReprieve) {
        checkReprieveParams(customerReprieve.getLossId(),customerReprieve.getMeasure());

        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());

        AssertUtil.isTrue(customerReprieveMapper.insertSelective(customerReprieve)<1,"添加暂缓数据失败！");


    }

    /**
     * 暂缓参数校验
     * @param lossId
     * @param measure
     */
    private void checkReprieveParams(Integer lossId, String measure) {
        AssertUtil.isTrue(null==lossId&&null==customerLossMapper.selectByPrimaryKey(lossId),"流失客户不存在！");

        AssertUtil.isTrue(StringUtils.isBlank(measure),"暂缓措施内容不能为空！");
    }

    /**
     * 更新暂缓
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateReprieve(CustomerReprieve customerReprieve) {

        AssertUtil.isTrue(null==customerReprieve.getId()&&null==customerReprieveMapper.selectByPrimaryKey(customerReprieve.getId()),"待更新记录不存在！");
        checkReprieveParams(customerReprieve.getLossId(),customerReprieve.getMeasure());


        customerReprieve.setUpdateDate(new Date());

        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve)<1,"更新暂缓数据失败！");
    }

    /**
     * 删除暂缓
     * @param repId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteReprieve(Integer repId) {
        AssertUtil.isTrue(null==repId,"系统异常，删除失败！");
        CustomerReprieve customerReprieve=customerReprieveMapper.selectByPrimaryKey(repId);
        AssertUtil.isTrue(null==customerReprieve,"待删除记录不存在！");

        customerReprieve.setUpdateDate(new Date());
        customerReprieve.setIsValid(0);

        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve)<1,"暂缓删除失败！");

    }
}

