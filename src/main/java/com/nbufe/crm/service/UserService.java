package com.nbufe.crm.service;

import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.UserMapper;
import com.nbufe.crm.dao.UserRoleMapper;
import com.nbufe.crm.model.UserModel;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.utils.Md5Util;
import com.nbufe.crm.utils.PhoneUtil;
import com.nbufe.crm.utils.UserIDBase64;
import com.nbufe.crm.vo.User;
import com.nbufe.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    //用户登录
    public UserModel userLogin(String userName, String userPwd){

        //判断用户名、密码是否为空
        checkUserParams(userName,userPwd);

        User user=userMapper.queryUserByUserName(userName);

        AssertUtil.isTrue(user==null,"用户不存在！");

        //判断密码是否正确
        checkUserPwd(userPwd,user.getUserPwd());

        //构建用户模型，并返回
        UserModel usermodel=bulidUserModel(user);
        return usermodel;
    }

    private UserModel bulidUserModel(User user) {
        UserModel userModel=new UserModel();
        //userModel.setUserId(user.getId());
        //获取加密userId
        String userIdStr= UserIDBase64.encoderUserID(user.getId());
        userModel.setUserIdStr(userIdStr);

        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     *
     * @param userPwd 前台传输的密码
     * @param password 数据库中的密码
     */
    private void checkUserPwd(String userPwd, String password) {
        userPwd= Md5Util.encode(userPwd);

        AssertUtil.isTrue(!userPwd.equals(password),"密码不正确！");
    }

    private void checkUserParams(String userName, String userPwd) {

        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空！");
    }

    /**
     * 修改密码
     * @param userId
     * @param userPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(Integer userId,String userPwd,String newPwd,String repeatPwd){
        User user=userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(user==null,"用户不存在！");
        checkUserPwdParams(userPwd,newPwd,repeatPwd,user.getUserPwd());
        user.setUserPwd(Md5Util.encode(newPwd));
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败！");
        user.setUpdateDate(new Date());

    }

    private void checkUserPwdParams(String userPwd, String newPwd, String repeatPwd, String pwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"原始密码不能为空!");
        AssertUtil.isTrue(!pwd.equals(Md5Util.encode(userPwd)),"原始密码不正确！");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空!");
        AssertUtil.isTrue(newPwd.equals(userPwd),"新密码与原始密码不能一致!");
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"重复密码不能为空!");
        AssertUtil.isTrue(!repeatPwd.equals(newPwd),"重复密码与新密码不一致!");


    }

    /**
     * 修改基本资料
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBasicInformation(User user) {
        AssertUtil.isTrue(user.getId()==null,"系统异常，保存失败！");

        AssertUtil.isTrue(null==user.getPhone(),"电话号码不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()),"电话号码格式不正确！");

        user.setUpdateDate(new Date());

        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"保存失败！");
    }



    /**
     * 用户添加
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        //数据校验
        checkAddUpdateUserParams(user);
        //设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));

        //执行添加操作
        //AssertUtil.isTrue(userMapper.insertSelective(user)<1,"用户添加失败！");



        //执行添加操作,返回主键
        AssertUtil.isTrue(userMapper.insertHasKey(user)<1,"用户添加失败！");

        //关联用户角色
        relationUserRole(user.getId(),user.getRoleIds());


    }

    /**
     * 关联用户角色
     * @param userId
     * @param roleIds
     */
    private void relationUserRole(Integer userId, String roleIds) {
        //判断用户是否有关联数据
        Integer count=userRoleMapper.countUserRoleByUserId(userId);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色分配失败！");
        }
        if(StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoleList=new ArrayList<>();
            String[] roleArray=roleIds.split(",");
            for (String roleId:roleArray) {
                UserRole userRole=new UserRole();
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(roleId));

                userRoleList.add(userRole);
            }

            //执行分配角色操作
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList)!=userRoleList.size(),"用户角色分配失败！");
        }
    }


    /**
     * 用户更新
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        //数据校验
        AssertUtil.isTrue(null==user.getId(),"系统异常，更新失败！");
        checkAddUpdateUserParams(user);
        //设置默认值
        user.setUpdateDate(new Date());

        //执行添加操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户更新失败！");
        //关联用户角色
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 添加更新用户数据校验
     * @param user
     */
    private void checkAddUpdateUserParams(User user) {
        User temp=userMapper.queryUserByUserName(user.getUserName());

        AssertUtil.isTrue(null==user.getUserName(),"用户名不能为空！");
        if(user.getId()==null){
            AssertUtil.isTrue(temp!=null,"用户名已存在！");
        }else{
            AssertUtil.isTrue(temp!=null&&!temp.getId().equals(user.getId()),"用户名已存在！");
        }

        AssertUtil.isTrue(null==user.getTrueName(),"真实姓名不能为空！");
        AssertUtil.isTrue(null==user.getEmail(),"邮箱不能为空！");
        AssertUtil.isTrue(null==user.getPhone(),"电话号码不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()),"电话号码格式不正确！");
    }


    /**
     * 用户删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] ids) {

        for (Integer userId:ids) {
            Integer count=userRoleMapper.countUserRoleByUserId(userId);
            if(count>1){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户删除失败！");
            }

        }
        AssertUtil.isTrue(ids==null&&ids.length<1,"待删除记录不存在！");
        AssertUtil.isTrue(userMapper.deleteBatch(ids)!=ids.length,"用户删除失败！");
    }


    /**
     * 查询所有的客户经理
     * @return
     */

    public List<Map<String, Object>> queryAllCustomerManager() {
        return userMapper.queryAllCustomerManager();
    }
}
