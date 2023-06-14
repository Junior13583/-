package com.example.junior.service.chatService;

import com.example.junior.dto.ChatRoomDTO;
import com.example.junior.dto.UserRoomDTO;
import com.example.junior.entity.ChatMsg;
import com.example.junior.entity.ChatRoom;
import com.example.junior.entity.UserRoom;
import com.example.junior.mapStruct.ChatRoomMapping;
import com.example.junior.mapper.ChatRoomMapper;
import com.example.junior.vo.ResponseDataVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @Description: 聊天室服務層实现类
* @Author: Junior
* @Date: 2023/6/12
*/
@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService{

    @Resource
    private ChatRoomMapper chatRoomMapper;

    @Override
    public ResponseDataVO getUserInfo(String ip) {
        List<UserRoom> userRooms = chatRoomMapper.queryUserRoomByIp(ip);

        return ResponseDataVO.customize(200, ip, userRooms);
    }

    @Override
    public ResponseDataVO insertChatRoom(String roomName, String ip) {
        ResponseDataVO res = ResponseDataVO.builder().msg("成功").build();
        // 查询房间是否存在
        List<ChatRoom> queryChatRoom = chatRoomMapper.queryChatRoom(roomName);

        if (!queryChatRoom.isEmpty()) {
            // 查询当前房间是否和用户ip绑定
            List<UserRoom> queryUserRoom = chatRoomMapper.queryUserRoom(ip, queryChatRoom.get(0).getRoomId());

            if (!queryUserRoom.isEmpty()) {
                res.setCode(201).setData("用户已添加该聊天室");
            } else if (queryChatRoom.get(0).getRoomId() == 1) {
                res.setCode(201).setData("系统共用聊天室不能再次添加");
            } else {
                // 用户ip和房间号绑定
                UserRoom userRoom = UserRoom.builder()
                        .userIp(ip)
                        .roomId(queryChatRoom.get(0).getRoomId())
                        .createTime(LocalDateTime.now())
                        .build();
                chatRoomMapper.insertUserRoom(userRoom);
                res.setCode(200).setData("加入聊天室成功");
            }

        } else {
            ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                    .roomName(roomName)
                    .creator(ip).build();
            ChatRoom chatRoom = ChatRoomMapping.INSTANCE.chatRoomDTOToChatRoom(chatRoomDTO);

            // 添加新的聊天室
            chatRoomMapper.insertChatRoom(chatRoom);

            // 同时将新的聊天室和用户ip绑定
            UserRoom userRoom = UserRoom.builder()
                    .userIp(ip)
                    .roomId(chatRoom.getRoomId())
                    .createTime(LocalDateTime.now())
                    .build();
            chatRoomMapper.insertUserRoom(userRoom);
            res.setCode(200).setData("添加聊天室成功");
        }

        return res;
    }

    @Override
    public ResponseDataVO deleteUserRoom(String roomName, String ip) {

        ResponseDataVO res = ResponseDataVO.builder().msg("成功").build();
        // 根据房间名获取房间id
        List<ChatRoom> queryChatRoom = chatRoomMapper.queryChatRoom(roomName);

        // 存在聊天室
        if (!queryChatRoom.isEmpty()) {
            UserRoom userRoom = UserRoom.builder()
                    .userIp(ip)
                    .roomId(queryChatRoom.get(0).getRoomId())
                    .build();

            //  获取当前聊天室id和用户ip是否存在绑定
            List<UserRoom> userRooms = chatRoomMapper.queryUserRoom(userRoom.getUserIp(), userRoom.getRoomId());

            if (!userRooms.isEmpty()) {
                // 删除绑定
                chatRoomMapper.deleteUserRoom(userRoom);
                res.setCode(200).setData("删除聊天室成功，后续能再次加入");
            }else {
                res.setCode(201).setData("当前用户下不存在该聊天室，请刷新页面");
            }
        } else {
            // 不存在聊天室
            res.setCode(201).setData("不存在该聊天室，请刷新页面");
        }


        return res;
    }

    @Override
    public PageInfo<ChatMsg> queryMsg(Integer pageIndex, String roomName, String ip) {

        // 根据房间名获取房间id
        List<ChatRoom> queryChatRoom = chatRoomMapper.queryChatRoom(roomName);

        // 创建page类
        PageHelper.startPage(pageIndex, 12);
        // 存在聊天室
        if (!queryChatRoom.isEmpty()) {
            Integer roomId = queryChatRoom.get(0).getRoomId();

            List<ChatMsg> chatMsgList = chatRoomMapper.queryMsg(roomId);
            PageInfo<ChatMsg> chatMsgPageInfo = new PageInfo<>(chatMsgList);

            // 根据用户ip将查询的数据分为自己发送和其他人发送
            List<ChatMsg> newChatMsgList = chatMsgList.stream().peek(chatMsg -> {
                if (chatMsg.getSender().equals(ip)) {
                    chatMsg.setPosition("right");
                }else {
                    chatMsg.setPosition("left");
                }
            }).collect(Collectors.toList());

            chatMsgPageInfo.setList(newChatMsgList);
            //  先构建 PageInfo 实例，保存页码相关参数
            return chatMsgPageInfo;
        }else {
            return new PageInfo<>();
        }



    }


}
