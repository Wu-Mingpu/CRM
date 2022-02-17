package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.query.DataDicQuery;
import com.nbufe.crm.service.DataDicService;
import com.nbufe.crm.vo.DataDic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("data_dic")
public class DataDicController extends BaseController {

    @Resource
    private DataDicService dataDicService;

    /**
     * 进入字典管理界面
     * @return
     */
    @RequestMapping("index")
    public String toDataDicPage(){
        return "dataDic/dataDic";
    }

    /**
     * 字典列表
     * @param dataDicQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryDataDicList(DataDicQuery dataDicQuery){
        return dataDicService.queryByParamsForTable(dataDicQuery);
    }

    /**
     * 添加或更新字典界面
     * @param dataDicId
     * @param request
     * @return
     */
    @RequestMapping("addOrUpdateDataDicPage")
    public String addOrUpdateDataDicPage(Integer dataDicId,HttpServletRequest request){
        if(dataDicId!=null){
            DataDic dataDic=dataDicService.selectByPrimaryKey(dataDicId);
            request.setAttribute("DataDic",dataDic);
        }
        return "dataDic/add_update";

    }

    /**
     * 添加字典
     * @param dataDic
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addDataDic(DataDic dataDic){
        dataDicService.addDataDic(dataDic);
        return success();
    }

    /**
     * 更新字典
     * @param dataDic
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateDataDic(DataDic dataDic){
        dataDicService.updateDataDic(dataDic);
        return success();
    }

    /**
     * 删除字典
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteDataDic(Integer[] ids){
        dataDicService.deleteDataDic(ids);
        return success();

    }

}
