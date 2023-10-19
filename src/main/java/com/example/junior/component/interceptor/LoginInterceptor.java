package com.example.junior.component.interceptor;

import com.example.junior.component.jwt.JwtUtil;
import com.example.junior.service.loginAndRegisterService.LoginAndRegisterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * @Description: 登录拦截，用来校验用户是否登录
 * @Author: Junior
 * @Date: 2023/10/13
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static final String LOGIN_PAGE = "/loginPage";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = "";
        // 从 cookies 中获得 token
        Cookie[] cookies = request.getCookies();
        Optional<Cookie[]> cookieOptional = Optional.ofNullable(cookies);
        if (cookieOptional.isPresent()) {
            Optional<String> tokenOptional = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(JwtUtil.USER_LOGIN_TOKEN))
                    .map(Cookie::getValue)
                    .findFirst();
            token = tokenOptional.orElse(null);
        }

        // 验证 token
        String sub = JwtUtil.validateToken(token);
        if (sub == null || sub.isEmpty()) {
            log.error("token 无法识别");
            response.sendRedirect(LOGIN_PAGE);
            return false;
        }

        // 更新 token 有效时间 (如果需要更新其实就是产生一个新的 token)
        if (JwtUtil.isNeedUpdate(token)) {
            LoginAndRegisterServiceImpl.setCookie(response, sub);
        }

        // 拦截器保留sub信息，方便后续链式调用中使用
        request.getSession().setAttribute("sub", sub);


        return true;
    }
}
