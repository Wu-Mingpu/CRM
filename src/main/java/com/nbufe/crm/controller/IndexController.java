package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.service.PermissionService;
import com.nbufe.crm.service.UserService;
import com.nbufe.crm.utils.LoginUserUtil;
import com.nbufe.crm.vo.Permission;
import com.nbufe.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;


    //系统登录页
    @RequestMapping("index")
    public String index(){
        return "index";
    }


    //系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    //后端管理主页面
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //登录成功后，查询当前用户登录信息
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        //通过id查询用户对象
        User user= (User) userService.selectByPrimaryKey(userId);
        //将用户对象存到请求域中
        request.setAttribute("user",user);

        //查询当前登录用户所拥有的权限
        List<String> permissions=permissionService.queryAllPermissionByRoleByUserId(userId);
        //将权限存到session作用域中
        request.getSession().setAttribute("permissions",permissions);



        return "main";
    }
}

