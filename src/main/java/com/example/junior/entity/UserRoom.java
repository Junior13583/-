package com.example.junior.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
* @Description: 用户ip房间号对应实体类
* @Author: Junior
* @Date: 2023/6/13
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class UserRoom {
    private Integer id;
    private String userIp;
    private Integer roomId;
    private String roomName;
    private LocalDateTime createTime;
}
