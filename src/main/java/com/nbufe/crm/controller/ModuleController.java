package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.model.TreeModel;
import com.nbufe.crm.service.ModuleService;
import com.nbufe.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    /**
     * 查询所有的资源
     * @return
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }

    /**
     * 查询资源列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }

    /**
     * 进入资源管理界面
     * @return
     */
    @RequestMapping("index")
    public String toModulePage(){
        return "module/module";
    }

    /**
     * 添加资源
     * @param module
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success();
    }

    /**
     * 更新资源
     * @param module
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success();
    }

    /**
     * 到菜单添加界面
     * @param grade
     * @param parentId
     * @return
     */
    @RequestMapping("toAddModulePage")
    public String toAddModulePage(Integer grade, Integer parentId, HttpServletRequest request){
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/add";


    }

    /**
     * 到菜单更新界面
     * @param moduleId
     * @param request
     * @return
     */
    @RequestMapping("toUpdateModulePage")
    public String toUpdateModulePage(Integer moduleId,HttpServletRequest request){
        Module module=moduleService.selectByPrimaryKey(moduleId);
        request.setAttribute("module",module);

        return "module/update";


    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);
        return success();

    }
}
