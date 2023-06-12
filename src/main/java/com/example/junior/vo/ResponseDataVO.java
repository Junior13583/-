package com.example.junior.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* @Description: 返回类型封装
* @Author: Junior
* @Date: 2023/3/2
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ResponseDataVO implements Serializable {
    private int code;
    private String msg;
    private Object data;

    /**
    * 返回成功结果封装，code: 200, msg: 请求成功, data: data由参数提供
    * @param data:  结果数据
    * @return: com.example.aopdemo.entity.ResponseData
    * @Author: Junior
    * @Date: 2023/3/2
    */
    public static ResponseDataVO success(Object data) {
        return ResponseDataVO.builder()
                .code(200)
                .msg("请求成功")
                .data(data).build();
    }

    /**
    * 请求失败结果封装，code: 404, msg: msg由参数提供, data: “”
    * @param msg:  错误信息
    * @return: com.example.aopdemo.entity.ResponseData
    * @Author: Junior
    * @Date: 2023/3/2
    */
    public static ResponseDataVO fail(String msg) {
        return ResponseDataVO.builder()
                .code(404)
                .msg(msg)
                .data("").build();
    }

    /**
    * 请求失败结果封装，code: code由参数提供, msg: msg由参数提供, data: “”
    * @param code:  错误状态码
	* @param msg:  错误信息
    * @return: com.example.aopdemo.entity.ResponseData
    * @Author: Junior
    * @Date: 2023/3/2
    */
    public static ResponseDataVO fail(int code, String msg) {
        return ResponseDataVO.builder()
                .code(code)
                .msg(msg)
                .data("").build();
    }

    /**
    * 自定义结果封装, code: code由参数提供, msg: msg由参数提供, data； data由参数提供
    * @param code:  自定义状态码
	* @param msg:  自定义信息
	* @param data:  返回结果数据
    * @return: com.example.aopdemo.entity.ResponseData
    * @Author: Junior
    * @Date: 2023/3/2
    */
    public static ResponseDataVO customize(int code, String msg, Object data) {
        return ResponseDataVO.builder()
                .code(code)
                .msg(msg)
                .data(data).build();
    }

    @Override
    public String toString() {
        return "{\"code\": " + code +
                ", \"msg\": \"" + msg + "\"" +
                ", \"data\": \"" + data + "\"" +
                "}";
    }
}
