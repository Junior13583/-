package com.example.junior.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
* @Description: 自定义线程池配置类
* @Author: Junior
* @Date: 2023/3/7
*/
@Configuration
public class MyThreadPoolConfig {
    @Value("${my-thread-pool.maxPoolSize}")
    private Integer maxPoolSize;
    @Value("${my-thread-pool.corePoolSize}")
    private Integer corePoolSize;
    @Value("${my-thread-pool.queueCapacity}")
    private Integer queueCapacity;
    @Value("${my-thread-pool.keepAliveSeconds}")
    private Integer keepAliveSeconds;
    @Value("${my-thread-pool.threadNamePrefix}")
    private String threadNamePrefix;
    @Value("${my-thread-pool.waitForTasksToCompleteOnShutdown}")
    private Boolean waitForTasksToCompleteOnShutdown;

    @Bean("myExecutor")		//注册为Bean，方便使用
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数等于系统核数--8核
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程满了之后由调用者所在的线程来执行
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 设置默认线程名称
        executor.setThreadNamePrefix(threadNamePrefix);
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        //执行初始化
        executor.initialize();
        return executor;
    }
}
