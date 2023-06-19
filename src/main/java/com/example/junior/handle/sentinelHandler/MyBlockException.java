package com.example.junior.handle.sentinelHandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.junior.handle.exceptHandler.customException.BusinessException;
import com.example.junior.vo.ResponseDataVO;

/**
* @Description: 自定义控制台限流熔断之后触发
* @Author: Junior
* @Date: 2023/4/13
*/
public class MyBlockException {
    public static ResponseDataVO allHandlerException(BlockException blockException) {
        return ResponseDataVO.fail("太拥挤了 ~ 请稍后重试");
    }

    // TODO 下面可以自定义其他兜底方法，注意使用 static 修饰。

    public static ResponseDataVO blackException(BlockException blockException) {
        return ResponseDataVO.fail("你已经被加入黑名单，请联系管理员！");
    }
}
