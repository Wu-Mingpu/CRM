package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.ModuleMapper;
import com.nbufe.crm.dao.PermissionMapper;
import com.nbufe.crm.dao.RoleMapper;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.vo.Module;
import com.nbufe.crm.vo.Permission;
import com.nbufe.crm.vo.Role;
import com.nbufe.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询角色列表
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空！");
        Role temp=roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp!=null,"用户名称已存在");

        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());

        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"角色添加失败！");

    }

    /**
     * 更新角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空！");
        Role temp=roleMapper.queryRoleByRoleName(role.getRoleName());

        AssertUtil.isTrue(null!=temp&&!(role.getId().equals(temp.getId())),"角色名称已存在！");

        role.setUpdateDate(new Date());

        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"角色更新失败！");

    }

    /**
     * 删除角色
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId) {
        roleMapper.deleteRole(roleId);
    }

    /**
     * 角色授权
     * @param roleId
     * @param mIds
     */
    public void addGrant(Integer roleId, Integer[] mIds) {
        AssertUtil.isTrue(roleId==null,"角色不存在！");

        Role role=roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role==null,"角色不存在！");

        //判断角色是否拥有资源
        Integer count=permissionMapper.countPermissionByRoleId(roleId);

        if(count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId)!=count,"角色分配失败！");
        }

        if(mIds!=null&&mIds.length>0) {
            List<Permission> permissionList = new ArrayList<>();
            for (Integer mId : mIds) {
                Permission permission = new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mId);
                permission.setRoleId(roleId);

                Module module = moduleMapper.selectByPrimaryKey(mId);
                permission.setAclValue(module.getOptValue());

                permissionList.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.addPermission(permissionList)!=permissionList.size(),"角色授权失败！");
        }
    }
}
