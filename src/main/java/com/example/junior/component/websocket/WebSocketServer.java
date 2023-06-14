package com.example.junior.component.websocket;

import com.example.junior.config.WebSocketConfigurator;
import com.example.junior.entity.ChatMsg;
import com.example.junior.entity.ChatRoom;
import com.example.junior.mapper.ChatRoomMapper;
import com.example.junior.vo.ResponseDataVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.stylesheets.LinkStyle;

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

import static javax.websocket.CloseReason.CloseCodes.NORMAL_CLOSURE;


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
    public void onOpen(Session session, @PathParam("room") String room) {
        //
        try {
            // todo 验证当前聊天室是否存在

            // 如果当前聊天室不在Map中，就添加
            SESSION_MAP.computeIfAbsent(room, k -> new ArrayList<>());

            String ip = session.getUserProperties().get("ip").toString();
            SESSION_MAP.get(room).add(session);
            log.info("用户【" + ip + "】：进入---> 【" + room + "】");
        } catch (Exception e) {
            onClose(session, room, new CloseReason(NORMAL_CLOSURE, "当前聊天室不存在"));
    }

    }

    @OnClose
    public void onClose(Session session, @PathParam("room") String room, CloseReason closeReason) {
        List<Session> sessionList = SESSION_MAP.get(room);
        String ip = session.getUserProperties().get("ip").toString();

        if (sessionList != null) {
            //  当用户关闭连接后，删除当前用户
            sessionList.remove(session);
            // 如何当前房间没有人，移除当前房间
            if (sessionList.isEmpty()) {
                SESSION_MAP.remove(room);
            }
        }

        log.info("用户【" + ip + "】：离开---> 【" + room + "】,原因是：" + closeReason);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("room") String room) throws IOException, IOException {
        // 获取当前房间的所有Client
        List<Session> sessionList = SESSION_MAP.get(room);
        String currentIp = session.getUserProperties().get("ip").toString();

        log.info("用户【" + currentIp + "】：在---> 【" + room + "】发送一条消息：" + message);
        // 判断聊天室是否存在
        List<ChatRoom> chatRooms = chatRoomMapper.queryChatRoom(room);

        if (!chatRooms.isEmpty()) {
            // 将消息存入数据库
            Integer roomId = chatRooms.get(0).getRoomId();
            ChatMsg chatMsg = ChatMsg.builder()
                    .roomId(roomId)
                    .sender(currentIp)
                    .msgType("text")
                    .content(message)
                    .sendTime(LocalDateTime.now())
                    .build();
            chatRoomMapper.insertMsg(chatMsg);

            // 将消息广播给其他客户端
            if (sessionList != null) {
                sessionList.forEach(sessionItem -> {
                    String clientIp = sessionItem.getUserProperties().get("ip").toString();
                    // 给除了自己的所有用户发送消息
                    if (!clientIp.equals(currentIp)) {
                        try {
                            // 转换为json字符串
                            String jsonMessage = new ObjectMapper().writeValueAsString(ResponseDataVO.customize(200, currentIp, message));
                            sessionItem.getBasicRemote().sendText(jsonMessage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }else {
            log.error("用户【" + currentIp + "】：在一个不存在的---> 【" + room + "】发送一条消息：" + message);
        }

    }

    @OnError
    public void onError (Session session, Throwable throwable) {
        log.error("出现错误");
        throwable.printStackTrace();
    }

}
