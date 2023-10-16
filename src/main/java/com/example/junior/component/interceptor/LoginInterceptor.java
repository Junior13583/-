package com.example.junior.component.interceptor;

import com.example.junior.component.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @Description: 登录拦截，用来校验用户是否登录
* @Author: Junior
* @Date: 2023/10/13
*/
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // http 的 header 中获得token
        String token = request.getHeader("authorization");

        // token 不存在
        if (token == null || token.isEmpty()) {
            System.out.println("UserLoginInterceptor class token is not exist");
            return false;
        }

        // 验证 token
        String sub = JwtUtil.validateToken(token);
        if (sub == null || sub.isEmpty()) {
            System.out.println("token exist, but not same");
            return false;
        }

        // 更新 token 有效时间 (如果需要更新其实就是产生一个新的 token)
        if (JwtUtil.isNeedUpdate(token)){
            String newToken = JwtUtil.createToken(sub);
            response.setHeader(JwtUtil.USER_LOGIN_TOKEN, newToken);
        }

        return true;
    }
}
