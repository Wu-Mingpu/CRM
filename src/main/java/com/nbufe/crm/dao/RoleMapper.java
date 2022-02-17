package com.nbufe.crm.dao;

import com.nbufe.crm.base.BaseMapper;
import com.nbufe.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    //查询角色列表
    public List<Map<String,Object>> queryAllRoles(Integer userId);

    //根据角色名称查询用户对象
    Role queryRoleByRoleName(String roleName);

    //删除角色
    Integer deleteRole(Integer roleId);
}