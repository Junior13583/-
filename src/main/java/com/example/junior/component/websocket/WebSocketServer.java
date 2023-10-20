package com.example.junior.component.websocket;

import com.example.junior.component.jwt.JwtUtil;
import com.example.junior.config.WebSocketConfigurator;
import com.example.junior.dto.ChatUserDTO;
import com.example.junior.entity.ChatMsg;
import com.example.junior.entity.ChatRoom;
import com.example.junior.entity.ChatUser;
import com.example.junior.mapStruct.ChatRoomMapping;
import com.example.junior.mapper.ChatRoomMapper;
import com.example.junior.vo.ResponseDataVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
* @Description: WebSocket服务端
* @Author: Junior
* @Date: 2023/6/10
*/
@Component
@ServerEndpoint(value = "/websocket/{room}", configurator = WebSocketConfigurator.class)
@Slf4j
public class WebSocketServer {


    private static final Map<String, List<Session>> SESSION_MAP = new ConcurrentHashMap<>();

    private static ChatRoomMapper chatRoomMapper;

    @Resource
    public void setChatRoomMapper(ChatRoomMapper chatRoomMapper) {
        WebSocketServer.chatRoomMapper = chatRoomMapper;
    }



    @OnOpen
    public void onOpen(Session session, @PathParam("room") String room) throws Exception {

        // 解析 session
        Map<String, String > sessionMap = analysisToken(session);
        String username = sessionMap.get("username");

        // 如果当前聊天室不在Map中，就添加
        SESSION_MAP.computeIfAbsent(room, k -> new ArrayList<>());

        SESSION_MAP.get(room).add(session);
        log.info("用户【" + username + "】：进入---> 【" + room + "】");


    }

    @OnClose
    public void onClose(Session session, @PathParam("room") String room, CloseReason closeReason) throws Exception {
        List<Session> sessionList = SESSION_MAP.get(room);
        // 解析 session
        Map<String, String> sessionMap = analysisToken(session);
        String username = sessionMap.get("username");


        if (sessionList != null) {
            //  当用户关闭连接后，删除当前用户
            sessionList.remove(session);
            // 如何当前房间没有人，移除当前房间
            if (sessionList.isEmpty()) {
                SESSION_MAP.remove(room);
            }
        }

        log.info("用户【" + username + "】：离开---> 【" + room + "】,原因是：" + closeReason);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("room") String room) throws Exception {
        // 获取当前房间的所有Client
        List<Session> sessionList = SESSION_MAP.get(room);
        // 解析 session
        Map<String, String> sessionMap = analysisToken(session);
        String username = sessionMap.get("username");
        String email = sessionMap.get("email");


        log.info("用户【" + username + "】：在---> 【" + room + "】发送一条消息：" + message);
        // 判断聊天室是否存在
        List<ChatRoom> chatRooms = chatRoomMapper.queryChatRoom(room);

        if (!chatRooms.isEmpty()) {
            // 将消息存入数据库
            Integer roomId = chatRooms.get(0).getRoomId();
            ChatMsg chatMsg = ChatMsg.builder()
                    .roomId(roomId)
                    .sender(email)
                    .msgType("text")
                    .content(message)
                    .sendTime(LocalDateTime.now())
                    .build();
            chatRoomMapper.insertMsg(chatMsg);

            // 将消息广播给其他客户端
            if (sessionList != null) {
                sessionList.forEach(sessionItem -> {
                    String clientEmail = sessionItem.getUserProperties().get("email").toString();
                    // 给除了自己的所有用户发送消息
                    if (!clientEmail.equals(email)) {
                        try {
                            Map<String, Object> data = new HashMap<>(10);
                            data.put("sender", email);
                            data.put("username", username);
                            data.put("content", message);
                            data.put("type", "text");
                            // 转换为json字符串
                            String jsonMessage = new ObjectMapper().writeValueAsString(ResponseDataVO.customize(200, "成功", data));
                            sessionItem.getBasicRemote().sendText(jsonMessage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }else {
            log.error("用户【" + username + "】：在一个不存在的---> 【" + room + "】发送一条消息：" + message);
        }

    }

    @OnError
    public void  onError (Session session, Throwable throwable) {
        log.error("出现错误,将重新登录");
    }

    /**
    * 对 token 认证，认证失败抛出错误，成功返回用户信息
    * @param token:  token
    * @return: java.lang.String
    * @Author: Junior
    * @Date: 2023/10/19
    */
    public String  validateToken(String token) throws Exception {
        String sub = JwtUtil.validateToken(token);

        // 如果解析不出用户信息，就返回登录页
        if (sub.isEmpty()) {
            throw new Exception("WebSocket 认证失败！");
        }else {
            return sub;
        }

    }

    /**
    * 解析 session，返回用户所有信息，以及对 session 补充用户信息
    * @param session:  websocket 会话信息
    * @return: java.util.Map<java.lang.String,java.lang.Object>
    * @Author: Junior
    * @Date: 2023/10/19
    */
    public Map<String, String> analysisToken(Session session) throws Exception {

        Map<String, String> resultMap = new HashMap<>(10);

        // 获取 token
        String token = session.getRequestParameterMap().get("token").get(0);
        // 对 token 进行校验，获取用户邮箱
        String email = validateToken(token);

        // 用邮箱获取用户名
        ChatUser chatUser = ChatUser.builder()
                .email(email).build();
        // 实体类转换为 DTO
        ChatUserDTO chatUserDTO = ChatRoomMapping.INSTANCE.chatUserToChatUserDTO(chatRoomMapper.queryUser(chatUser));

        // 往 session 中存入用户名和邮箱信息
        String username = chatUserDTO.getName();
        session.getUserProperties().put("username", username);
        session.getUserProperties().put("email", email);

        resultMap.put("username", username);
        resultMap.put("email", email);

        return resultMap;
    }

    public void broadcastMsg(String roomName, String email, MultipartFile file, String fileName, byte[] fileByte) throws IOException {
        // 获取当前房间的所有Client
        List<Session> sessionList = SESSION_MAP.get(roomName);

        // 通过 email 获取用户信息
        ChatUser chatUser = ChatUser.builder()
                        .email(email).build();
        ChatUserDTO chatUserDTO = ChatRoomMapping.INSTANCE.chatUserToChatUserDTO(chatRoomMapper.queryUser(chatUser));

        log.info("用户【" + email + "】：在---> 【" + roomName + "】发送一份文件：" + file.getOriginalFilename());
        // 判断聊天室是否存在
        List<ChatRoom> chatRooms = chatRoomMapper.queryChatRoom(roomName);

        if (!chatRooms.isEmpty()) {
            // 获取当前时间
            LocalDateTime nowTime = LocalDateTime.now();

            // 获取文件类型
            String fileType = getCustomizeFileType(file.getContentType());
            // 获取文件真名
            String realName = file.getOriginalFilename();

            String downloadUrl = "";
            // 拼接下载链接
            downloadUrl = "/download?fileName=" + fileName + "&roomName=" + roomName + "&alias=" + realName;
            final String content = downloadUrl;

            // 将消息存入数据库
            Integer roomId = chatRooms.get(0).getRoomId();
            ChatMsg chatMsg = ChatMsg.builder()
                    .roomId(roomId)
                    .sender(email)
                    .msgType(fileType)
                    .content(content)
                    .filename(realName)
                    .filesize(file.getSize())
                    .sendTime(nowTime)
                    .build();
            chatRoomMapper.insertMsg(chatMsg);

            // 将消息广播给其他客户端
            if (sessionList != null) {
                sessionList.forEach(sessionItem -> {
                    String clientEmail = sessionItem.getUserProperties().get("email").toString();
                    // 给除了自己的所有用户发送消息
                    if (!clientEmail.equals(email)) {
                        try {
                            Map<String, Object> data = new HashMap<>(10);
                            data.put("sender", email);
                            data.put("username", chatUserDTO.getName());
                            data.put("content", content);
                            data.put("type", fileType);
                            data.put("name", file.getOriginalFilename());
                            data.put("size", file.getSize());
                            // 转换为json字符串
                            String jsonMessage = new ObjectMapper().writeValueAsString(ResponseDataVO.customize(200, "成功", data));
                            sessionItem.getBasicRemote().sendText(jsonMessage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }else {
            log.error("用户【" + chatUserDTO.getName() + "】：在一个不存在的---> 【" + roomName + "】发送一份文件：" + file.getOriginalFilename());
        }
    }

    /**
    * 根据文件类型获取自定义的文件类型
    * @param fileType:  fileType
    * @return: java.lang.String
    * @Author: Junior
    * @Date: 2023/6/14
    */
    public String getCustomizeFileType(String fileType) {

        String suffixName = "image/";
        if (fileType != null && fileType.startsWith(suffixName)){
            fileType = "image";
        }else {
            fileType = "others";
        }
        return fileType;
    }

}
