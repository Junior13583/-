package com.example.junior.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 消息实体类
 * @Author: Junior
 * @Date: 2023/6/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ChatMsgDTO {
    private Integer msgId;
    private Integer roomId;
    private String sender;
    private String msgType;
    private String content;
    private LocalDateTime sendTime;
}
