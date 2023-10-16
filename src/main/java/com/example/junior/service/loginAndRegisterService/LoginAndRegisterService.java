package com.example.junior.service.loginAndRegisterService;

import com.example.junior.vo.ResponseDataVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @Description: 登录和注册服务接口
* @Author: Junior
* @Date: 2023/10/13
*/
public interface LoginAndRegisterService {

    /**
    * 登录接口
    * @param email:  用户邮箱
	* @param password:  用户密码，密码长度限制 1-6
	* @param request:
	* @param response:
    * @return: com.example.junior.vo.ResponseDataVO
    * @Author: Junior
    * @Date: 2023/10/13
    */
    ResponseDataVO login(HttpServletRequest request, HttpServletResponse response, String email, String password) throws IOException;

    /**
    * 注册接口
    * @param name:  用户名
	* @param email:  用户邮箱
	* @param password:  用户密码，密码长度限制 1-6
    * @return: com.example.junior.vo.ResponseDataVO
    * @Author: Junior
    * @Date: 2023/10/13
    */
    ResponseDataVO register(String name, String email, String password);
}
