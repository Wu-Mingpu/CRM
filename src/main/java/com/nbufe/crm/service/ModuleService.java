package com.nbufe.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.dao.ModuleMapper;
import com.nbufe.crm.dao.PermissionMapper;
import com.nbufe.crm.model.TreeModel;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 查询所有的资源(zTree)
     * @return
     */
    public List<TreeModel> queryAllModules(Integer roleId){
        List<TreeModel> moduleList=moduleMapper.queryAllModules(roleId);
        List<Integer> moduleIds=permissionMapper.queryModuleIdsByRoleId(roleId);
        for (TreeModel model:moduleList) {
            if(moduleIds.contains(model.getId())){
                model.setChecked(true);
            }


        }

        return moduleList;

    }

    /**
     * 查询资源列表
     * @return
     */
    public Map<String,Object> queryModuleList(){
        Map<String,Object> result = new HashMap<String,Object>();
        List<Module> moduleList=moduleMapper.queryModuleList();
        result.put("count",moduleList.size());
        result.put("data",moduleList);
        result.put("code",0);
        result.put("msg","");
        return result;

    }

    /**
     * 添加资源
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module) {
        //1.参数校验
        //菜单层级 非空且为0，1，2
        AssertUtil.isTrue(module.getGrade() == null, "菜单层级不能为空！");
        AssertUtil.isTrue(!(module.getGrade() == 0 || module.getGrade() == 1 || module.getGrade() == 2), "菜单层级有误！");

        //菜单名称 非空且同一层级名称不重复
        AssertUtil.isTrue(module.getModuleName() == null, "菜单名称不能为空！");
        AssertUtil.isTrue(null!=moduleMapper.queryModuleByNameAndGrade(module.getModuleName(), module.getGrade()), "菜单名称已存在！");

        // url 二级菜单 非空且同一层级不重复
        if (module.getGrade() == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "URL地址不能为空！");
            AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndUrl(module.getGrade(), module.getUrl()), "URL地址不可重复！");

        }

        //grade=1,2 非空且父级菜单必须存在
        if(module.getGrade()==0){
            module.setParentId(-1);
        }
        if(module.getGrade()!=0){
            AssertUtil.isTrue(null == module.getParentId(),"父级菜单不能为空！");
            AssertUtil.isTrue(null == moduleMapper.selectByPrimaryKey(module.getParentId()), "请指定正确的父级菜单！");
        }

        //权限码 非空且不可重复
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空！");
        AssertUtil.isTrue(null != moduleMapper.queryModuleByOptValue(module.getOptValue()),"权限码已存在！");


        //2.设置参数默认值
        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());

        //3.执行添加操作
        AssertUtil.isTrue(moduleMapper.insertSelective(module) < 1, "添加资源失败！");

    }

    /**
     * 更新资源
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {
        //1.参数校验

        // id 非空，数据存在
        Module temp = moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(null == temp || null == module.getId(), "待更新记录不存在！");

        //菜单层级 非空且为0，1，2
        AssertUtil.isTrue(module.getGrade() == null, "菜单层级不能为空！");
        AssertUtil.isTrue(!(module.getGrade() == 0 || module.getGrade() == 1 || module.getGrade() == 2), "菜单层级有误！");

        //菜单名称 非空且同一层级名称不重复（不包含当前修改记录本身）
        AssertUtil.isTrue(module.getModuleName() == null, "菜单名称不能为空！");
        temp=moduleMapper.queryModuleByNameAndGrade(module.getModuleName(), module.getGrade());
        AssertUtil.isTrue(null!=temp&&!((temp.getId()).equals(module.getId())), "菜单名称已存在！");

        // url 二级菜单 非空且同一层级不重复（不包含当前修改记录本身）
        if (module.getGrade() == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "URL地址不能为空！");
            temp=moduleMapper.queryModuleByGradeAndUrl(module.getGrade(), module.getUrl());
            AssertUtil.isTrue(null != temp&&!((temp.getId()).equals(module.getId())), "URL地址不可重复！");

        }

        //权限码 非空且不可重复（不包含当前修改记录本身）
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空！");
        temp=moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null != temp&&!((temp.getId()).equals(module.getId())),"权限码已存在！");


        //2.设置参数默认值
        module.setUpdateDate(new Date());

        //3.执行添加操作
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) < 1, "更新资源失败！");
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer id) {
        //id 不为空且记录存在
        Module temp=moduleMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null==id||null==temp,"待删除记录不存在！");

        //待删除菜单不存在子菜单
        Integer count = moduleMapper.queryModuleByParentId(id);
        AssertUtil.isTrue(count > 0, "该目录存在子目录，不可删除！");

        //删除指定资源id的权限记录
        count= permissionMapper.countPermissionByModuleId(id);
        if(count>0){
            permissionMapper.deletePermissionByModuleId(id);
        }

        //设置默认值
        temp.setIsValid((byte) 0);
        temp.setUpdateDate(new Date());

        // 执行更新
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp) < 1, "删除资源失败！");

    }
}
