package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.PermissionMapper;
import com.nbufe.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {


    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 通过用户id查询用户权限
     * @param userId
     * @return
     */
    public List<String> queryAllPermissionByRoleByUserId(Integer userId) {
        return permissionMapper.queryAllPermissionByRoleByUserId(userId);
    }
}
