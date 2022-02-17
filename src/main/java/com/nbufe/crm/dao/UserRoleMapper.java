package com.nbufe.crm.dao;

import com.nbufe.crm.base.BaseMapper;
import com.nbufe.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    //通过用户id查询用户关联角色的数量
    Integer countUserRoleByUserId(Integer userId);

    //通过用户id删除用户关联角色
    Integer deleteUserRoleByUserId(Integer userId);
}