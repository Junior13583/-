package com.example.junior.mapStruct;

import com.example.junior.dto.ChatRoomDTO;
import com.example.junior.dto.ChatUserDTO;
import com.example.junior.dto.UserRoomDTO;
import com.example.junior.entity.ChatRoom;
import com.example.junior.entity.ChatUser;
import com.example.junior.entity.UserRoom;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* @Description: DO类和DTO类转换
* @Author: Junior
* @Date: 2023/6/12
*/
@Mapper
public interface ChatRoomMapping {
    ChatRoomMapping INSTANCE = Mappers.getMapper(ChatRoomMapping.class);

    /**
    * 将ChatRoomDTO类转换成ChatRoom类
    * @param chatRoomDTO:  chatRoomDTO
    * @return: com.example.junior.entity.ChatRoom
    * @Author: Junior
    * @Date: 2023/6/12
    */
    ChatRoom chatRoomDTOToChatRoom(ChatRoomDTO chatRoomDTO);

    /**
    * 将ChatRoom类转换成ChatRoomDTO类
    * @param chatRoom:  chatRoom
    * @return: com.example.junior.dto.ChatRoomDTO
    * @Author: Junior
    * @Date: 2023/6/12
    */
    ChatRoomDTO chatRoomToChatRoomDTO(ChatRoom chatRoom);

    /**
    * 将多个ChatRoom类转换成多个ChatRoomDTO类
    * @param chatRooms:  chatRooms
    * @return: java.util.List<com.example.junior.dto.ChatRoomDTO>
    * @Author: Junior
    * @Date: 2023/6/13
    */
    List<ChatRoomDTO> chatRoomDTOListToChatRoomList(List<ChatRoom> chatRooms);

    /**
    * 将UserRoomDTO转换为UserRoom类
    * @param userRoomDTO:  userRoomDTO
    * @return: com.example.junior.entity.UserRoom
    * @Author: Junior
    * @Date: 2023/6/13
    */
    UserRoom userRoomDTOToUserRoom(UserRoomDTO userRoomDTO);

    /**
    * 将UserRoom转换为UserRoomDTO类
    * @param userRoom:  userRoom
    * @return: com.example.junior.dto.UserRoomDTO
    * @Author: Junior
    * @Date: 2023/6/13
    */
    UserRoomDTO userRoomTOUserRoomDTO(UserRoom userRoom);

    /**
    * ChatUserDTO 转换为 ChatUser 类
    * @param chatUserDTO:  chatUserDTO
    * @return: com.example.junior.entity.ChatUser
    * @Author: Junior
    * @Date: 2023/10/16
    */
    ChatUser chatUserDTOToChatUser(ChatUserDTO chatUserDTO);

    /**
    * ChatUser 类转换为 ChatUserDTO 类
    * @param chatUser:  chatUser
    * @return: com.example.junior.dto.ChatUserDTO
    * @Author: Junior
    * @Date: 2023/10/16
    */
    ChatUserDTO chatUserToChatUserDTO(ChatUser chatUser);

}
