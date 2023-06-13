package com.example.junior.service.chatService;

import com.example.junior.dto.ChatRoomDTO;
import com.example.junior.dto.UserRoomDTO;
import com.example.junior.entity.ChatRoom;
import com.example.junior.vo.ResponseDataVO;

import java.util.Map;
import java.util.Objects;

/**
* @Description: 聊天室服務層
* @Author: Junior
* @Date: 2023/6/12
*/
public interface ChatRoomService {

    /**
    * 通过ip获取该ip拥有的由于聊天室
    * @param ip:  ip
    * @return: com.example.junior.vo.ResponseDataVO
    * @Author: Junior
    * @Date: 2023/6/13
    */
    ResponseDataVO getUserInfo(String ip);

    /**
    * 添加聊天室
    * @param roomName:  聊天室名称
    * @param ip:  用户ip
    * @return: String
    * @Author: Junior
    * @Date: 2023/6/12
    */
    ResponseDataVO insertChatRoom(String  roomName, String ip);

    /**
    * 删除用户ip和房间id绑定
    * @param ip:  ip
    * @param roomName:  roomName
    * @return: void
    * @Author: Junior
    * @Date: 2023/6/13
    */
    ResponseDataVO deleteUserRoom(String roomName, String ip);



}
