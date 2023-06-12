package com.example.junior.controller;

import com.example.junior.dto.ChatRoomDTO;
import com.example.junior.service.chatService.ChatRoomServiceImpl;
import com.example.junior.vo.ResponseDataVO;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 聊天室控制层
 * @Author: Junior
 * @Date: 2023/6/10
 */
@Controller
@Validated
public class ChatRoomController {

    @Resource
    private ChatRoomServiceImpl chatRoomService;

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @PostMapping("/getIp")
    @ResponseBody
    public ResponseDataVO getIp(HttpServletRequest request) {

        return ResponseDataVO.success(ipInfo(request));
    }

    @PostMapping("/addChat")
    @ResponseBody
    public ResponseDataVO addChat(HttpServletRequest request, @RequestParam @Length(min = 3, max = 12) String  roomName) {
        String ip = ipInfo(request);
        String res = chatRoomService.insertChatRoom(roomName, ip);
        return ResponseDataVO.success(res);
    }

    public String ipInfo(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
