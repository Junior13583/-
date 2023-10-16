package com.example.junior.service.loginAndRegisterService;

import com.example.junior.component.jwt.JwtUtil;
import com.example.junior.vo.ResponseDataVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @Description: 登录注册接口实现类
* @Author: Junior
* @Date: 2023/10/13
*/
@Service
public class LoginAndRegisterServiceImpl implements LoginAndRegisterService{
    @Override
    public ResponseDataVO login(HttpServletRequest request, HttpServletResponse response, String email, String password) throws IOException {
        setCookie(response, email);

        return ResponseDataVO.success("登录成功");
    }

    @Override
    public ResponseDataVO register(String name, String email, String password) {
        return ResponseDataVO.success("注册成功");
    }

    public static void setCookie(HttpServletResponse response, String email) {
        String token = JwtUtil.createToken(email);
        String tokenValue = token.split(" ")[1];
        Cookie cookie = new Cookie(JwtUtil.USER_LOGIN_TOKEN, tokenValue);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }
}
