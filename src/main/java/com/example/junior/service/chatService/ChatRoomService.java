package com.example.junior.service.chatService;

import com.example.junior.dto.ChatRoomDTO;
import com.example.junior.entity.ChatRoom;

/**
* @Description: 聊天室服務層
* @Author: Junior
* @Date: 2023/6/12
*/
public interface ChatRoomService {

    /**
    * 添加聊天室
    * @param roomName:  聊天室名称
    * @param ip:  用户ip
    * @return: String
    * @Author: Junior
    * @Date: 2023/6/12
    */
    String  insertChatRoom(String  roomName, String ip);



}
