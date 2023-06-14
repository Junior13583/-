package com.example.junior.controller;

import com.example.junior.dto.ChatRoomDTO;
import com.example.junior.dto.UserRoomDTO;
import com.example.junior.entity.ChatMsg;
import com.example.junior.service.chatService.ChatRoomServiceImpl;
import com.example.junior.vo.ResponseDataVO;
import com.github.pagehelper.PageInfo;
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

    @PostMapping("/getUserInfo")
    @ResponseBody
    public ResponseDataVO getIp(HttpServletRequest request) {

        return chatRoomService.getUserInfo(ipInfo(request));
    }

    @PostMapping("/addChat")
    @ResponseBody
    public ResponseDataVO addChat(HttpServletRequest request, @RequestParam @Length(min = 3, max = 12) String  roomName) {
        String ip = ipInfo(request);

        return chatRoomService.insertChatRoom(roomName, ip);
    }

    @PostMapping("/delChat")
    @ResponseBody
    public ResponseDataVO delChat(HttpServletRequest request, @RequestParam String roomName) {
        String ip = ipInfo(request);

        return chatRoomService.deleteUserRoom(roomName, ip);
    }

    @PostMapping("/loadingMsg")
    @ResponseBody
    public ResponseDataVO loadingMsg(HttpServletRequest request, @RequestParam String roomName, @RequestParam Integer pageIndex) {
        String ip = ipInfo(request);
        PageInfo<ChatMsg> pageInfo = chatRoomService.queryMsg(pageIndex, roomName, ip);
        return ResponseDataVO.success(pageInfo);
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
