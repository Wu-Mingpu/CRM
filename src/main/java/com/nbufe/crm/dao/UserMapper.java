package com.nbufe.crm.dao;

import com.nbufe.crm.base.BaseMapper;
import com.nbufe.crm.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    //通过用户名查询用户对象
    public User queryUserByUserName(String userName);


    //查询所有的客户经理
    public List<Map<String,Object>> queryAllCustomerManager();




}