package com.example.junior.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

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
public class ChatRoomDTO {
    private Integer roomId;
    private String roomName;
    private String creator;
}
