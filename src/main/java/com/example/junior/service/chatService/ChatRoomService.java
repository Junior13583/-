package com.example.junior.service.chatService;

import com.example.junior.dto.ChatMsgDTO;
import com.example.junior.dto.ChatUserDTO;
import com.example.junior.entity.ChatMsg;
import com.example.junior.vo.ResponseDataVO;
import com.github.pagehelper.PageInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @Description: 聊天室服務層
* @Author: Junior
* @Date: 2023/6/12
*/
public interface ChatRoomService {

    /**
    * 通过用户信息获取该用户拥有的由于聊天室
    * @param chatUserDTO:  用户对象
    * @return: com.example.junior.vo.ResponseDataVO
    * @Author: Junior
    * @Date: 2023/6/13
    */
    ResponseDataVO getUserInfo(ChatUserDTO chatUserDTO);

    /**
    * 添加聊天室
    * @param roomName:  聊天室名称
    * @param email:  用户邮箱
    * @return: String
    * @Author: Junior
    * @Date: 2023/6/12
    */
    ResponseDataVO insertChatRoom(String  roomName, String email);

    /**
    * 删除用户邮箱和房间id绑定
    * @param email:  用户邮箱
    * @param roomName:  roomName
    * @return: void
    * @Author: Junior
    * @Date: 2023/6/13
    */
    ResponseDataVO deleteUserRoom(String roomName, String email);

    /**
    * 上传文件
    * @param roomName:  roomName
     * @param email:  用户邮箱
     * @param file:  file
     * @param index:  前端发送的文件位置，需要再次返回前端用于定位作用
    * @exception IOException: io异常
    * @return: com.example.junior.vo.ResponseDataVO
    * @Author: Junior
    * @Date: 2023/6/14
    */
    ResponseDataVO uploadFile(String roomName, String email, MultipartFile file, Integer index) throws IOException;

    /**
    * 文件下载接口
    * @param roomName:  roomName
	* @param fileName:  文件别名
    * @return: com.example.junior.vo.ResponseDataVO
    * @Author: Junior
    * @Date: 2023/6/14
    */
    Resource downloadFile(String roomName, String fileName);

    /**
    * 根据房间名字和用户ip查询所有消息
    * @param pageIndex:  pageIndex
     * @param roomName:  roomName
     * @param email:  用户邮箱
    * @return: com.github.pagehelper.PageInfo<com.example.junior.entity.ChatMsg>
    * @Author: Junior
    * @Date: 2023/6/14
    */
    PageInfo<ChatMsg> queryMsg(Integer pageIndex, String roomName, String email);



}
