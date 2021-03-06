package com.kirago.chapter19.shiro;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
* @Description:    java类作用描述
* @Author:         kirago
* @CreateDate:     2019/3/29 4:41 PM
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTFilter.class);

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response){
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception{
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String authorization = httpServletRequest.getHeader("Authorization");
        JWTToken jwtToken = new JWTToken(authorization);

        getSubject(request,response).login(jwtToken);

        return true;

    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,Object mappedValue){
        if(isLoginAttempt(request, response)){
            try {
                executeLogin(request, response);
            } catch (Exception e){
                response401(request, response);
            }
        }
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    private void response401(ServletRequest req, ServletResponse resp) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.sendRedirect("/401");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
