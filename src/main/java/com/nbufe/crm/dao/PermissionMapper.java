package com.nbufe.crm.dao;

import com.nbufe.crm.base.BaseMapper;
import com.nbufe.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    //通过用户id查询所有的权限
   List<String> queryAllPermissionByRoleByUserId(Integer userId);


    //通过角色id查询资源数量
    Integer countPermissionByRoleId(Integer roleId);

    //通过角色id删除资源
    Integer deletePermissionByRoleId(Integer roleId);

    //添加角色资源
    int addPermission(List<Permission> permissionList);

    //通过角色id查询角色拥有的资源
    List<Integer> queryModuleIdsByRoleId(Integer roleId);

    // 通过资源ID查询权限记录
    Integer countPermissionByModuleId(Integer id);

    // 通过资源ID删除权限记录
    Integer deletePermissionByModuleId(Integer id);
}