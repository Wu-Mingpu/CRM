package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.exceptions.ParamsException;
import com.nbufe.crm.model.UserModel;
import com.nbufe.crm.query.UserQuery;
import com.nbufe.crm.service.UserService;
import com.nbufe.crm.utils.LoginUserUtil;
import com.nbufe.crm.vo.User;
import org.omg.PortableInterceptor.RequestInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd){
        ResultInfo resultInfo=new ResultInfo();

        /*try{
            UserModel userModel=userService.userLogin(userName,userPwd);
            resultInfo.setResult(userModel);

        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        }catch (Exception e){
            resultInfo.setCode(300);
            resultInfo.setMsg("操作失败！");
        }*/
        UserModel userModel=userService.userLogin(userName,userPwd);
        resultInfo.setResult(userModel);

        return resultInfo;
    }

    /**
     * 修改密码
     * @param request
     * @param userPwd
     * @param newPwd
     * @param repeatPwd
     * @return
     */
    @PostMapping("updateUserPwd")
    @ResponseBody
    public ResultInfo updateUserPwd(HttpServletRequest request,String userPwd,String newPwd,String repeatPwd){
        ResultInfo resultInfo=new ResultInfo();
        /*try{
            //从cookie中获取用户ID
            Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
            userService.updateUser(userId,userPwd,newPwd,repeatPwd);
        }catch (ParamsException p){
            p.printStackTrace();
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(300);
            resultInfo.setMsg("修改密码失败！");
        }*/
        //从cookie中获取用户ID
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updateUser(userId,userPwd,newPwd,repeatPwd);
        return resultInfo;
    }

    //跳转到修改密码界面
    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    /**
     * 到基本资料界面
     * @return
     */
    @RequestMapping("toSettingPage")
    public String toSettingPage(HttpServletRequest request){
        Integer userId=LoginUserUtil.releaseUserIdFromCookie(request);
        if(userId!=null){
            User user=userService.selectByPrimaryKey(userId);
            request.setAttribute("user",user);
        }

        return "user/setting";
    }


    /**
     * 更新基本资料
     * @param user
     * @return
     */
    @RequestMapping("updateBasicInformation")
    @ResponseBody
    public ResultInfo updateBasicInformation(User user){
        userService.updateBasicInformation(user);

        return success();
    }
    /**
     * 多条件查询查询用户列表
     * @param userQuery
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryUserList(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 到用户管理界面
     * @return
     */
    @RequestMapping("index")
    public String toUserPage(){
        return "user/user";
    }

    /**
     * 到添加更新用户界面
     * @return
     */
    @RequestMapping("toAddUpdateUserPage")
    public String toAddUpdateUserPage(Integer userId,HttpServletRequest request){
        if(userId!=null){
            User user=userService.selectByPrimaryKey(userId);
            request.setAttribute("user",user);

        }
        return "user/add_update";
    }

    /**
     * 用户添加
     * @param user
     */

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success();
    }

    /**
     * 用户更新
     * @param user
     */

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success();
    }

    /**
     * 用户删除
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUser(ids);
        return success();
    }

    /**
     * 查询所有的客户经理
     * @return
     */
    @RequestMapping("queryAllCustomerManager")
    @ResponseBody
    public List<Map<String,Object>> queryAllCustomerManager(){
        return userService.queryAllCustomerManager();
    }
}
