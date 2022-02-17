package com.nbufe.crm;

import com.alibaba.fastjson.JSON;
import com.nbufe.crm.base.ResultInfo;
import com.nbufe.crm.exceptions.AuthException;
import com.nbufe.crm.exceptions.NoLoginException;
import com.nbufe.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * 全局异常处理类
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) {

        //判断是否为未登录异常
        if(e instanceof NoLoginException){
            ModelAndView mv=new ModelAndView("redirect:/index");
            return mv;

        }
        ModelAndView modelAndView=new ModelAndView();
        //默认异常处理
        modelAndView.setViewName("error");
        modelAndView.addObject("msg","操作失败！，请重试！");

        //得到方法对象HandlerMethod
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod= (HandlerMethod) handler;

            //得到方法
            Method method=handlerMethod.getMethod();

            //判断方法上是否有指定注解
            ResponseBody responseBody=method.getDeclaredAnnotation(ResponseBody.class);

            if(responseBody==null){
                //方法返回视图
                if(e instanceof ParamsException){
                    ParamsException p= (ParamsException) e;
                    modelAndView.setViewName("error");
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());

                }else if(e instanceof AuthException) {
                    AuthException p = (AuthException) e;
                    modelAndView.setViewName("error");
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg", p.getMsg());
                }

                return modelAndView;
            }else{
                //方法返回JSON
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("操作失败，请重试！");

                if(e instanceof ParamsException){
                    ParamsException p= (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());

                }else if(e instanceof AuthException){
                    AuthException p= (AuthException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());

                }

                //设置响应类型和编码格式
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                PrintWriter out=null;
                try{
                    out=httpServletResponse.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                    out.close();

                } catch (Exception ex) {
                    ex.printStackTrace();

                }

                return null;

            }


        }

        return modelAndView;
    }
}
