package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.DataDicMapper;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.vo.DataDic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

@Service
public class DataDicService extends BaseService<DataDic,Integer> {

    @Resource
    private DataDicMapper dataDicMapper;


    /**
     * 添加字典
     * @param dataDic
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addDataDic(DataDic dataDic) {
        /*1.数据校验*/
        AssertUtil.isTrue(StringUtils.isBlank(dataDic.getDataDicName()),"字典名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(dataDic.getDataDicName()),"字典值不能为空！");

        /*2.设置默认值*/
        dataDic.setCreateDate(new Date());
        dataDic.setUpdateDate(new Date());
        dataDic.setIsValid((byte) 1);

        /*3.执行添加操作*/
        AssertUtil.isTrue(dataDicMapper.insertSelective(dataDic)<1,"字典添加失败！");
    }

    /**
     * 更新字典
     * @param dataDic
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDataDic(DataDic dataDic) {
        /*1.数据校验*/
        AssertUtil.isTrue(null==dataDic.getId()||null==dataDicMapper.selectByPrimaryKey(dataDic.getId()),"待更新记录不存在！");
        AssertUtil.isTrue(StringUtils.isBlank(dataDic.getDataDicName()),"字典名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(dataDic.getDataDicName()),"字典值不能为空！");

        /*2.设置默认值*/
        dataDic.setUpdateDate(new Date());

        /*3.执行添加操作*/
        AssertUtil.isTrue(dataDicMapper.updateByPrimaryKeySelective(dataDic)<1,"字典更新失败！");

    }

    /**
     * 删除字典
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDataDic(Integer[] ids) {
        AssertUtil.isTrue(null==ids||ids.length<1,"待删除记录不存在！");
        AssertUtil.isTrue(dataDicMapper.deleteBatch(ids)!= ids.length,"字典删除失败！");
    }
}
