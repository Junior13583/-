package com.example.junior.component.asyncTask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
* @Description: 异步任务
* @Author: Junior
* @Date: 2023/3/7
*/
@EnableAsync
@Slf4j
@Component
public class AsyncTask {

    @Async("myExecutor")
    public void asyncTask(long times){
        long j = 0L;
        for (long i = 0L; i < times; i++) {
            j++;
        }
        log.info("[线程-" + Thread.currentThread().getName() + "] : 执行了"+ j);
    }

    @Async("myExecutor")
    public void asyncTaskExcept(long times, long exceptNum){
        long j = 0L;
        for (long i = 0L; i < times; i++) {
            if (i == exceptNum) {
                int a = 1 / 0;
            }
            j++;
        }
        log.info("[线程-" + Thread.currentThread().getName() + "] : 执行了"+ j);
    }
}
