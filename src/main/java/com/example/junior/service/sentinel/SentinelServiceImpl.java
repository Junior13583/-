package com.example.junior.service.sentinel;

import org.springframework.stereotype.Service;

/**
* @Description: Sentinel 服务监控实现类
* @Author: Junior
* @Date: 2023/4/13
*/
@Service
public class SentinelServiceImpl implements SentinelService{

    @Override
    public String hello(String limit) {
        return limit;
    }


}
