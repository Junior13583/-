package com.example.junior.mapStruct;

import com.example.junior.dto.ChatRoomDTO;
import com.example.junior.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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

}
