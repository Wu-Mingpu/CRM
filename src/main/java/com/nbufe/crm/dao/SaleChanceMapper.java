package com.nbufe.crm.dao;

import com.nbufe.crm.base.BaseMapper;
import com.nbufe.crm.vo.SaleChance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer>{

    //查询所有的销售人员
    public List<Map<String,Object>> queryAllSales();

}