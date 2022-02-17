package com.nbufe.crm.interceptor;

import com.nbufe.crm.dao.UserMapper;
import com.nbufe.crm.exceptions.NoLoginException;
import com.nbufe.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 非法请求拦截器
 */

public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //通过Cookie获得userId
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        if(userId==null||userMapper.selectByPrimaryKey(userId)==null){
            throw new NoLoginException();
        }
        return true;
    }
}
