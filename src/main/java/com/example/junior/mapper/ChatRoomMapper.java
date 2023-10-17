package com.example.junior.mapper;

import com.example.junior.entity.ChatMsg;
import com.example.junior.entity.ChatRoom;
import com.example.junior.entity.ChatUser;
import com.example.junior.entity.UserRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @Description: 聊天室的數據接口
* @Author: Junior
* @Date: 2023/6/12
*/
@Repository
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
    List<ChatRoom> queryChatRoom(String roomName);

    /**
    * 插入用户ip和房间id对应表
    * @param userRoom:  roomId
    * @return: void
    * @Author: Junior
    * @Date: 2023/6/13
    */
    void insertUserRoom(UserRoom userRoom);

    /**
    * 根据用户ip和房间号查询
    * @param ip:  ip
     * @param roomId:  roomId
    * @return: java.util.List<com.example.junior.entity.UserRoom>
    * @Author: Junior
    * @Date: 2023/6/13
    */
    List<UserRoom> queryUserRoom(String ip, Integer roomId);

    /**
    * 通过用户ip查询所有聊天室
    * @param ip:  ip
    * @return: java.util.List<com.example.junior.entity.UserRoom>
    * @Author: Junior
    * @Date: 2023/6/13
    */
    List<UserRoom> queryUserRoomByIp(String ip);

    /**
    * 删除用户ip和房间id绑定
    * @param userRoom:  userRoom
    * @return: void
    * @Author: Junior
    * @Date: 2023/6/13
    */
    void deleteUserRoom(UserRoom userRoom);

    /**
    * 将聊天室的消息保存起来
    * @param chatMsg:  chatMsg
    * @return: void
    * @Author: Junior
    * @Date: 2023/6/13
    */
    void insertMsg(ChatMsg chatMsg);

    /**
    * 根据房间id和用户ip获取该房间所有聊天记录
    * @param roomId:  roomId
    * @return: java.util.List<com.example.junior.entity.ChatMsg>
    * @Author: Junior
    * @Date: 2023/6/13
    */
    List<ChatMsg> queryMsg(Integer roomId);

    /**
    * 查询用户
    * @param chatUser:  chatUser
    * @return: com.example.junior.entity.ChatUser
    * @Author: Junior
    * @Date: 2023/10/17
    */
    ChatUser queryUser(ChatUser chatUser);

    /**
    * 插入用户
    * @param chatUser:  chatUser
    * @return: void
    * @Author: Junior
    * @Date: 2023/10/17
    */
    void insertUser(ChatUser chatUser);

}
