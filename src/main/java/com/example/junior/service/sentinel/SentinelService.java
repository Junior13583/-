package com.example.junior.service.sentinel;

/**
* @Description: Sentinel 服务监控
* @Author: Junior
* @Date: 2023/4/13
*/
public interface SentinelService {
    /**
    * 限制流量
    * @param limit:  limit
    * @return: java.lang.String
    * @Author: Junior
    * @Date: 2023/4/13
    */
    String hello(String limit);
}
