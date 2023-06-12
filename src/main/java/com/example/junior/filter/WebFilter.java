package com.example.junior.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
* @Description: 拦截器，用于获取用户ip提供websocket使用
* @Author: Junior
* @Date: 2023/6/10
*/
@javax.servlet.annotation.WebFilter(filterName = "sessionFilter",urlPatterns = "/websocket/*")
@Order(1)
public class WebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req= (HttpServletRequest) request;
        req.getSession().setAttribute("ip",req.getRemoteHost());
        chain.doFilter(request,response);
    }
}