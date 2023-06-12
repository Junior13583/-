package com.example.junior.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
* @Description: 聊天室实体类
* @Author: Junior
* @Date: 2023/6/12
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ChatRoom {
    private Integer roomId;
    private String roomName;
    private String creator;
}
