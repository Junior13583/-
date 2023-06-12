package com.example.junior.handle.exceptHandler.customException;


import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* @Description: 自定义异常类
* @Author: Junior
* @Date: 2023/3/2
*/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class BusinessException extends RuntimeException implements Serializable {

    private String errorMsg;

    @Override
    public String toString() {
        String className = BusinessException.class.getName();
        return className + ": " + errorMsg;
    }
}
