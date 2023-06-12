package com.example.junior.service.chatService;

import com.example.junior.dto.ChatRoomDTO;
import com.example.junior.entity.ChatRoom;
import com.example.junior.mapStruct.ChatRoomMapping;
import com.example.junior.mapper.ChatRoomMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @Description: 聊天室服務層实现类
* @Author: Junior
* @Date: 2023/6/12
*/
@Service
public class ChatRoomServiceImpl implements ChatRoomService{

    @Resource
    private ChatRoomMapper chatRoomMapper;

    @Override
    public String  insertChatRoom(String roomName, String ip) {
        String res = "";
        ChatRoom queryChatRoom = chatRoomMapper.queryChatRoom(roomName);
        if (queryChatRoom != null) {
            res = "加入聊天室成功";
        } else {
            ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                    .roomName(roomName)
                    .creator(ip).build();
            ChatRoom chatRoom = ChatRoomMapping.INSTANCE.chatRoomDTOToChatRoom(chatRoomDTO);
            chatRoomMapper.insertChatRoom(chatRoom);
            res = "添加聊天室成功";
        }

        return res;
    }


}
