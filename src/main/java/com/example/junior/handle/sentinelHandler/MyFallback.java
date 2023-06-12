package com.example.junior.handle.sentinelHandler;

import com.example.junior.vo.ResponseDataVO;

/**
* @Description: 自定义回调，遇到程序遇到问题之后触发降级操作
* @Author: Junior
* @Date: 2023/4/13
*/
public class MyFallback {
    public static ResponseDataVO allFallback() {
        return ResponseDataVO.fail("服务器错误，稍后再试！！");
    }

    // TODO 下面可以自定义其他回调方法，注意使用 static 修饰
}
