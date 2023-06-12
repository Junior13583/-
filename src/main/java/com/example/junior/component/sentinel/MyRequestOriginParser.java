package com.example.junior.component.sentinel;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
* @Description: 自定义 RequestOriginParser ip黑名单设置
* @Author: Junior
* @Date: 2023/4/13
*/
@Component
public class MyRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        String origin = request.getHeader("origin");
        if(StringUtils.isBlank(origin)){
            // 配置 ip 黑名单
            origin = request.getRemoteAddr();
        }
        if (StringUtils.isBlank(origin)) {
            throw new IllegalArgumentException("请求参数不合法");
        }
        return origin;
    }
}
