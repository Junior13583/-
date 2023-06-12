package com.example.junior.service.multithreading;


/**
* @Description: 多线程测试服务
* @Author: Junior
* @Date: 2023/3/7
*/

public interface MultithreadingService {
    /**
    * 测试异步任务服务
    * @param times:  执行次数
    * @return: void
    * @Author: Junior
    * @Date: 2023/3/7
    */
    void testMulti(long times);

    /**
    * 测试异步任务中出现异常
    * @param times:  执行次数
	* @param exceptNum:  发生异常的位置
    * @return: void
    * @Author: Junior
    * @Date: 2023/3/7
    */
    void testMultiExcept(long times, long exceptNum);
}
