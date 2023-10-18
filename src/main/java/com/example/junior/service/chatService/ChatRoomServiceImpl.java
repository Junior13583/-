package com.example.junior.service.chatService;

import com.example.junior.component.websocket.WebSocketServer;
import com.example.junior.dto.ChatMsgDTO;
import com.example.junior.dto.ChatRoomDTO;
import com.example.junior.dto.ChatUserDTO;
import com.example.junior.entity.ChatMsg;
import com.example.junior.entity.ChatRoom;
import com.example.junior.entity.ChatUser;
import com.example.junior.entity.UserRoom;
import com.example.junior.mapStruct.ChatRoomMapping;
import com.example.junior.mapper.ChatRoomMapper;
import com.example.junior.vo.ResponseDataVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @Description: 聊天室服務層实现类
* @Author: Junior
* @Date: 2023/6/12
*/
@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService{

    @Autowired
    private ChatRoomMapper chatRoomMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Value("${websocket.protocol}")
    private String protocol;

    @Value("${websocket.port}")
    private String port;

    @Value("${server.address}")
    private String address;

    @Override
    public ResponseDataVO getUserInfo(ChatUserDTO chatUserDTO) {
        List<UserRoom> userRooms = chatRoomMapper.queryUserRoomByEmail(chatUserDTO.getEmail());
        Map<String, Object> map = new HashMap<>(10);
        String wsUrl = protocol + address + ":"+ port + "/websocket/";

        map.put("wsUrl", wsUrl);
        map.put("ip", chatUserDTO.getName());
        map.put("rooms", userRooms);

        return ResponseDataVO.customize(200, "成功", map);
    }

    @Override
    public ResponseDataVO insertChatRoom(String roomName, String email) {
        ResponseDataVO res = ResponseDataVO.builder().msg("成功").build();
        // 查询房间是否存在
        List<ChatRoom> queryChatRoom = chatRoomMapper.queryChatRoom(roomName);

        if (!queryChatRoom.isEmpty()) {
            // 查询当前房间是否和用户邮箱绑定
            List<UserRoom> queryUserRoom = chatRoomMapper.queryUserRoom(email, queryChatRoom.get(0).getRoomId());

            if (!queryUserRoom.isEmpty()) {
                res.setCode(201).setData("用户已添加该聊天室");
            } else if (queryChatRoom.get(0).getRoomId() == 1) {
                res.setCode(201).setData("系统共用聊天室不能再次添加");
            } else {
                // 用户邮箱和房间号绑定
                UserRoom userRoom = UserRoom.builder()
                        .userEmail(email)
                        .roomId(queryChatRoom.get(0).getRoomId())
                        .createTime(LocalDateTime.now())
                        .build();
                chatRoomMapper.insertUserRoom(userRoom);
                res.setCode(200).setData("加入聊天室成功");
            }

        } else {
            ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                    .roomName(roomName)
                    .creator(email).build();
            ChatRoom chatRoom = ChatRoomMapping.INSTANCE.chatRoomDTOToChatRoom(chatRoomDTO);

            // 添加新的聊天室
            chatRoomMapper.insertChatRoom(chatRoom);

            // 同时将新的聊天室和用户邮箱绑定
            UserRoom userRoom = UserRoom.builder()
                    .userEmail(email)
                    .roomId(chatRoom.getRoomId())
                    .createTime(LocalDateTime.now())
                    .build();
            chatRoomMapper.insertUserRoom(userRoom);
            res.setCode(200).setData("添加聊天室成功");
        }

        return res;
    }

    @Override
    public ResponseDataVO deleteUserRoom(String roomName, String email) {

        ResponseDataVO res = ResponseDataVO.builder().msg("成功").build();
        // 根据房间名获取房间id
        List<ChatRoom> queryChatRoom = chatRoomMapper.queryChatRoom(roomName);

        // 存在聊天室
        if (!queryChatRoom.isEmpty()) {
            UserRoom userRoom = UserRoom.builder()
                    .userEmail(email)
                    .roomId(queryChatRoom.get(0).getRoomId())
                    .build();

            //  获取当前聊天室id和用户邮箱是否存在绑定
            List<UserRoom> userRooms = chatRoomMapper.queryUserRoom(userRoom.getUserEmail(), userRoom.getRoomId());

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
    public ResponseDataVO uploadFile(String roomName, String email, MultipartFile file, Integer index) throws IOException {

        String filename = file.getOriginalFilename();
        Path roomPath = Paths.get(uploadPath, roomName);
        try {
            if (!Files.exists(roomPath)) {
                Files.createDirectories(roomPath);
            }

            // 将文件读成byte
            byte[] fileBytes = file.getBytes();
            // 为了区分同名文件，所有文件保存使用当前时间戳作为文件名
            String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf('.') + 1);
            String fileName = Long.toString(Instant.now().toEpochMilli()) + '.' + suffix;
            // 保存文件到服务器
            file.transferTo(new File(Paths.get(uploadPath, roomName, fileName).toString()));

            // 通过websocket向其他客户端发送消息
            webSocketServer.broadcastMsg(roomName, email, file, fileName, fileBytes);
            return ResponseDataVO.success(index);

        } catch (Throwable throwable) {
            log.error("文件发送失败" + throwable.toString());
            return ResponseDataVO.customize(404, "失败", index);
        }

    }

    @Override
    public Resource downloadFile(String roomName, String fileName) {
        Path filePath = Paths.get(uploadPath, roomName).resolve(fileName).normalize();
        File file = filePath.toFile();

        Resource resource = new FileSystemResource(file);

        if (!resource.exists()) {
            throw new RuntimeException("文件已经失效");
        }

        return resource;
    }

    @Override
    public PageInfo<ChatMsg> queryMsg(Integer pageIndex, String roomName, String email) {

        // 根据房间名获取房间id
        List<ChatRoom> queryChatRoom = chatRoomMapper.queryChatRoom(roomName);

        // 创建page类
        PageHelper.startPage(pageIndex, 12);
        // 存在聊天室
        if (!queryChatRoom.isEmpty()) {
            Integer roomId = queryChatRoom.get(0).getRoomId();

            List<ChatMsg> chatMsgList = chatRoomMapper.queryMsg(roomId);
            PageInfo<ChatMsg> chatMsgPageInfo = new PageInfo<>(chatMsgList);

            // 根据用户邮箱将查询的数据分为自己发送和其他人发送
            List<ChatMsg> newChatMsgList = chatMsgList.stream().peek(chatMsg -> {
                // 添加聊天气泡位置
                if (chatMsg.getSender().equals(email)) {
                    chatMsg.setPosition("right");
                }else {
                    chatMsg.setPosition("left");
                }
                // 将消息发送者邮箱改为发送者名字
                ChatUser chatUser = ChatUser.builder()
                        .email(email).build();
                chatMsg.setSender(chatRoomMapper.queryUser(chatUser).getName());

            }).collect(Collectors.toList());

            chatMsgPageInfo.setList(newChatMsgList);
            //  先构建 PageInfo 实例，保存页码相关参数
            return chatMsgPageInfo;
        }else {
            return new PageInfo<>();
        }



    }


}
