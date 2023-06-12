package com.example.junior.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
* @Description: 全局方法监控PO类，提供需要记录日志的参数类型，可以根据自身的项目需要修改此类。
* @Author: Junior
* @Date: 2023/3/7
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class MethodRecordsDTO {
    private String method;
    private String status;
    private long spendTime;
    private String params;
    private String result;

    @Override
    public String toString() {
        return "运行方法: '" + method + '\'' +
                ", 状态: " + status +
                ", 耗时: " + spendTime + "ms" +
                ", 参数: [" + params + ']' +
                ", 返回值: " + result;
    }
}
