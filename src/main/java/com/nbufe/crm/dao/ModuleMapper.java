package com.nbufe.crm.dao;

import com.nbufe.crm.base.BaseMapper;
import com.nbufe.crm.model.TreeModel;
import com.nbufe.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    //查询所有的资源(zTree)
    List<TreeModel> queryAllModules(Integer roleId);

    //查询资源列表
    List<Module> queryModuleList();

    // 通过层级与模块名查询资源对象
    Module queryModuleByNameAndGrade(@Param("moduleName") String moduleName,@Param("grade") Integer grade );

    // 通过层级与URL查询资源对象
    Module queryModuleByGradeAndUrl(@Param("grade")Integer grade, @Param("url")String url);

    // 通过权限码查询资源对象
    Module queryModuleByOptValue(String optValue);

    // 查询指定资源是否存在子记录
    Integer queryModuleByParentId(Integer id);
}