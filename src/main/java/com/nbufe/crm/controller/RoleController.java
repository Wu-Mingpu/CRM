package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.BaseQuery;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.query.RoleQuery;
import com.nbufe.crm.service.RoleService;
import com.nbufe.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class

RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 查询所有角色
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    /**
     * 多条件查询角色列表
     * @param roleQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryRoleList(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);

    }

    /**
     * 进入角色管理界面
     * @return
     */
    @RequestMapping("index")
    public String toRolePage(){
        return "role/role";
    }

    /**
     * 进入添加更新角色界面
     * @return
     */
    @RequestMapping("toAddUpdateRolePage")
    public String toAddUpdateRolePage(Integer roleId, HttpServletRequest request){
        if(roleId!=null){
            Role role=roleService.selectByPrimaryKey(roleId);
            request.setAttribute("role",role);
        }
        return "role/add_update";

    }

    /**
     * 添加角色
     * @param role
     * @return
     */

    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success();
    }

    /**
     * 更新角色
     * @param role
     * @return
     */

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success();
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId){
        roleService.deleteRole(roleId);
        return success();
    }

    /**
     * 进入到角色授权界面
     * @return
     */
    @RequestMapping("toRoleGrantPage")
    public String toRoleGrantPage(Integer roleId,HttpServletRequest request){

        request.setAttribute("roleId",roleId);
        return "role/grant";
    }

    /**
     * 角色授权
     * @param roleId
     * @param mIds
     * @return
     */

    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){
        roleService.addGrant(roleId,mIds);
        return success();
    }
}
