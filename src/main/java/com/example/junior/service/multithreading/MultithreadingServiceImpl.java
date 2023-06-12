package com.example.junior.service.multithreading;

import com.example.junior.component.asyncTask.AsyncTask;
import com.example.junior.service.multithreading.MultithreadingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @Description: 多线程测试实现类
* @Author: Junior
* @Date: 2023/3/7
*/

@Slf4j
@Service
public class MultithreadingServiceImpl implements MultithreadingService {

    @Resource
    private AsyncTask asyncTask;

    @Override
    public void testMulti(long times) {
        asyncTask.asyncTask(times);
    }

    @Override
    public void testMultiExcept(long times, long exceptNum) {
        asyncTask.asyncTaskExcept(times, exceptNum);
    }

}
