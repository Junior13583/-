package com.example.junior.component.aop;

import com.example.junior.dto.MethodRecordsDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;



/**
* @Description: 异步任务监控处理类
* @Author: Junior
* @Date: 2023/3/7
*/
@Aspect
@Component
@Slf4j
public class AsyncTaskAspect {

    @Pointcut("execution(* com.example.junior.component.asyncTask.*.*(..))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object handleAsyncMethod(ProceedingJoinPoint joinPoint) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            result = joinPoint.proceed();
            String status = "成功";
            MethodRecordsDTO methodRecordsDTO = getMethodRecords(beginTime, joinPoint, result, status);
            log.info("【异步任务监控服务】: " + methodRecordsDTO.toString());

        } catch (Throwable e) {
            String status = " 出现：" + e + " 错误";
            MethodRecordsDTO methodRecordsDTO = getMethodRecords(beginTime, joinPoint, result, status);
            log.error("【异步任务监控服务】: " + methodRecordsDTO.toString() ,e);
        }
        return result;
    }

    public MethodRecordsDTO getMethodRecords(long beginTime, ProceedingJoinPoint joinPoint, Object result, String status) {
        String methodName = joinPoint.getSignature().toString();
        Object[] params =joinPoint.getArgs();
        StringBuilder paramsStr=new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i == params.length - 1) {
                paramsStr.append(params[i].toString());
            }else {
                paramsStr.append(params[i].toString()).append("; ");
            }
        }

        return MethodRecordsDTO.builder()
                .method(methodName)
                .params(paramsStr.toString())
                .status(status)
                .spendTime(System.currentTimeMillis() - beginTime)
                .result(result != null ? result.toString() : "void")
                .build();
    }

}
