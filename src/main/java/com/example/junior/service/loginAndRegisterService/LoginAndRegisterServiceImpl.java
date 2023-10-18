package com.example.junior.service.loginAndRegisterService;

import com.example.junior.component.jwt.JwtUtil;
import com.example.junior.dto.ChatUserDTO;
import com.example.junior.entity.ChatUser;
import com.example.junior.mapStruct.ChatRoomMapping;
import com.example.junior.mapper.ChatRoomMapper;
import com.example.junior.vo.ResponseDataVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
* @Description: 登录注册接口实现类
* @Author: Junior
* @Date: 2023/10/13
*/
@Service
public class LoginAndRegisterServiceImpl implements LoginAndRegisterService{

    @Resource
    private ChatRoomMapper chatRoomMapper;

    @Override
    public ResponseDataVO login(HttpServletRequest request, HttpServletResponse response, String email, String password) throws IOException {

        ChatUserDTO chatUserDTO = ChatUserDTO.builder()
                .email(email)
                .build();
        ChatUser chatUser = ChatRoomMapping.INSTANCE.chatUserDTOToChatUser(chatUserDTO);

        // 通过用户邮箱获取用户信息
        ChatUser existUser = chatRoomMapper.queryUser(chatUser);
        Optional<ChatUser> optionalChatUser = Optional.ofNullable(existUser);
        if (optionalChatUser.isPresent()) {
            // 校验密码
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (passwordEncoder.matches(password, existUser.getPassword())) {
                // 校验成功设置 cookie
                setCookie(response, email);
                return ResponseDataVO.success("登录成功");
            } else {
                return ResponseDataVO.fail("登录失败，请检查用户名和密码！！");
            }
        }

        return ResponseDataVO.fail("用户不存在，请先注册！！");
    }

    @Override
    public ResponseDataVO register(String name, String email, String password) {

        // 加密用户密码，然后获取插入用户对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        ChatUserDTO newUserDTO = ChatUserDTO.builder()
                .name(name)
                .email(email)
                .password(hashedPassword).build();
        ChatUser newUser = ChatRoomMapping.INSTANCE.chatUserDTOToChatUser(newUserDTO);

        // 先校验邮箱是否被占用
        ChatUser existUser = chatRoomMapper.queryUser(newUser);
        Optional<ChatUser> optionalChatUser = Optional.ofNullable(existUser);
        if (!optionalChatUser.isPresent()) {
            // 当前邮箱未被使用，用户才能注册成功
            chatRoomMapper.insertUser(newUser);
            return ResponseDataVO.success("注册成功");
        }
        return ResponseDataVO.fail("注册失败，当前邮箱已被使用");
    }

    /**
    * 生成 jwt 加密同时保存到 cookies 中
    * @param response:  response
	* @param email:  email
    * @return: void
    * @Author: Junior
    * @Date: 2023/10/17
    */
    public static void setCookie(HttpServletResponse response, String email) {
        String token = JwtUtil.createToken(email);
        String tokenValue = token.split(" ")[1];
        Cookie cookie = new Cookie(JwtUtil.USER_LOGIN_TOKEN, tokenValue);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    /**
    * 从 request 中获取到用户信息
    * @param request:  request
    * @return: com.example.junior.dto.ChatUserDTO
    * @Author: Junior
    * @Date: 2023/10/17
    */
    public ChatUserDTO jwtInfo(HttpServletRequest request) {

        String email = request.getSession().getAttribute("sub").toString();
        ChatUser chatUser = ChatUser.builder()
                .email(email).build();

        return ChatRoomMapping.INSTANCE.chatUserToChatUserDTO(chatRoomMapper.queryUser(chatUser));
    }
}
