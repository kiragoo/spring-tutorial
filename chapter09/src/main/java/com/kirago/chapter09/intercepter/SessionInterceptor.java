package com.kirago.chapter09.intercepter;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @Description:    java类作用描述
* @Author:         kirago
* @CreateDate:     2019/3/6 6:09 PM
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        System.out.println(request.getRequestURI());
        //登录不做拦截
        if(request.getRequestURI().equals("/user/login") || request.getRequestURI().equals("/user/login_view"))
        {
            return true;
        }
        //验证session是否存在
        Object obj = request.getSession().getAttribute("_session_user");
        if(obj == null)
        {
            response.sendRedirect("/user/login_view");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}