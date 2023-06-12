package com.example.junior.component.aop;


import com.example.junior.dto.ApiRecordsDTO;
import com.example.junior.handle.exceptHandler.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


/**
 * @Description: 接口监控处理类。
 * @Author: Junior
 * @Date: 2023/3/2
 */
@Component
@Aspect
@Slf4j
public class ControllerAspect {

    @Resource
    private ExceptionHandler exceptionHandler;

    @Pointcut("execution(* com.example.junior.controller.*.*(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object handleControllerMethod(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        HttpServletRequest request = null;
        try {
            // 前置通知
            request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            // 执行方法
            result = point.proceed();
            // 后置通知
            String status = "成功";
            ApiRecordsDTO records = getApiRecords(request, beginTime, result, point, status);
            log.info("【接口监控服务】:"+ records.toString());
        } catch (Throwable e) {
            // 异常通知
            assert request != null;
            String status = "出现：" + e + " 错误";
            ApiRecordsDTO records = getApiRecords(request, beginTime, result, point, status);
            log.error("【接口监控服务】:" + records, e);
            return exceptionHandler.exception(e);
        }finally {
            // 最终通知

        }
        return result;

    }

    public ApiRecordsDTO getApiRecords(HttpServletRequest request, long beginTime, Object result,
                                       ProceedingJoinPoint point, String status) {
        MethodSignature methodSignature =(MethodSignature) point.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] param =point.getArgs();
        StringBuilder str=new StringBuilder();
        if(parameterNames!=null){
            for(int i=0;i<parameterNames.length;i++){
                if (null !=param[i]){
                    if (i == parameterNames.length - 1) {
                        str.append(param[i].toString());
                    }else {
                        str.append(param[i].toString()).append("; ");
                    }
                }
            }
        }

        return ApiRecordsDTO.builder()
                .url(request.getRequestURL().toString())
                .method(request.getMethod())
                .status(status)
                .params(str.toString())
                .spendTime(System.currentTimeMillis() - beginTime)
                .result(result != null ? result.toString() : "void").build();
    }
}
