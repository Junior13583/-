package com.example.junior.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.example.junior.dto.ChatUserDTO;
import com.example.junior.entity.ChatMsg;
import com.example.junior.handle.sentinelHandler.MyBlockException;
import com.example.junior.handle.sentinelHandler.MyFallback;
import com.example.junior.service.chatService.ChatRoomServiceImpl;
import com.example.junior.service.loginAndRegisterService.LoginAndRegisterServiceImpl;
import com.example.junior.vo.ResponseDataVO;
import com.github.pagehelper.PageInfo;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @Description: 聊天室控制层
 * @Author: Junior
 * @Date: 2023/6/10
 */
@Controller
@Validated
public class ChatRoomController {

    @Autowired
    private ChatRoomServiceImpl chatRoomService;

    @Autowired
    private LoginAndRegisterServiceImpl loginAndRegisterService;

    @GetMapping("/loginPage")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseDataVO login(HttpServletRequest request, HttpServletResponse response, @RequestParam @Email String email, @RequestParam @Pattern(regexp = "^[a-zA-Z0-9]{8,}$") String password) throws IOException {
        return loginAndRegisterService.login(request, response, email, password);
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseDataVO register(@RequestParam @Length(min = 1, max = 6) String name, @RequestParam @Email(message = "Invalid email address") String email, @RequestParam @Pattern(regexp = "^[a-zA-Z0-9]{8,}$") String password) {
        return loginAndRegisterService.register(name, email, password);
    }

    @GetMapping("/")
    @SentinelResource(value = "index", defaultFallback = "allFallback", fallbackClass = {MyFallback.class},
            blockHandler = "blackException", blockHandlerClass = {MyBlockException.class})
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @PostMapping("/getUserInfo")
    @ResponseBody
    public ResponseDataVO getIp(HttpServletRequest request) {

        return chatRoomService.getUserInfo(loginAndRegisterService.jwtInfo(request));
    }

    @PostMapping("/addChat")
    @ResponseBody
    public ResponseDataVO addChat(HttpServletRequest request, @RequestParam @Length(min = 3, max = 12) String  roomName) {
        ChatUserDTO chatUserDTO = loginAndRegisterService.jwtInfo(request);
        String email = chatUserDTO.getEmail();

        return chatRoomService.insertChatRoom(roomName, email);
    }

    @PostMapping("/delChat")
    @ResponseBody
    public ResponseDataVO delChat(HttpServletRequest request, @RequestParam String roomName) {
        ChatUserDTO chatUserDTO = loginAndRegisterService.jwtInfo(request);
        String email = chatUserDTO.getEmail();

        return chatRoomService.deleteUserRoom(roomName, email);
    }

    @PostMapping("/uploadFile")
    @ResponseBody
    public ResponseDataVO uploadFile(HttpServletRequest request, @RequestParam String roomName,
                                     @RequestParam MultipartFile file, @RequestParam Integer index) throws IOException {
        ChatUserDTO chatUserDTO = loginAndRegisterService.jwtInfo(request);
        String email = chatUserDTO.getEmail();

        return chatRoomService.uploadFile(roomName, email, file, index);
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> download(@RequestParam String fileName, @RequestParam String roomName, @RequestParam String alias) throws UnsupportedEncodingException {
        Resource resource = chatRoomService.downloadFile(roomName, fileName);
        String encodedFileName = UriUtils.encodePath(alias, StandardCharsets.UTF_8.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

    @PostMapping("/loadingMsg")
    @ResponseBody
    public ResponseDataVO loadingMsg(HttpServletRequest request, @RequestParam String roomName, @RequestParam Integer pageIndex) {
        ChatUserDTO chatUserDTO = loginAndRegisterService.jwtInfo(request);
        String email = chatUserDTO.getEmail();
        PageInfo<ChatMsg> pageInfo = chatRoomService.queryMsg(pageIndex, roomName, email);
        return ResponseDataVO.success(pageInfo);
    }


}
