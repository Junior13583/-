package com.example.junior.mapper;

import com.example.junior.entity.ChatRoom;
/**
* @Description: 聊天室的數據接口
* @Author: Junior
* @Date: 2023/6/12
*/
public interface ChatRoomMapper {

    /**
    * 添加聊天室
    * @param chatRoom:  chatRoom
    * @return: void
    * @Author: Junior
    * @Date: 2023/6/12
    */
    void  insertChatRoom(ChatRoom chatRoom);

    /**
    * 根据聊天室名字查询聊天室
    * @param roomName:  roomName
    * @return: com.example.junior.entity.ChatRoom
    * @Author: Junior
    * @Date: 2023/6/12
    */
    ChatRoom queryChatRoom(String roomName);

}
