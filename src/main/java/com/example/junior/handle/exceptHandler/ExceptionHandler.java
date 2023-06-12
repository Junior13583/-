package com.example.junior.handle.exceptHandler;

import com.example.junior.handle.exceptHandler.customException.BusinessException;
import com.example.junior.vo.ResponseDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 异常处理类，用于处理全局的异常，根据不同的异常类型可以分别做不同的处理模式。
 * @Author: Junior
 * @Date: 2023/3/2
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandler {


    /**
     * 处理执行过程中出现的异常
     *
     * @param e: e
     * @return: com.example.junior.vo.ResponseData
     * @Author: Junior
     * @Date: 2023/3/9
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseDataVO exception(Throwable e) {

        //  处理 BusinessException 异常
        if (e instanceof BusinessException) {
            return businessExceptionHandle(e);
        }

        //  处理 POST 参数为实体类校验异常
        if (e instanceof MethodArgumentNotValidException) {
            return methodArgumentNotValidExceptionHandle(e);
        }

        //  处理 GET 参数为单个参数或者多个参数
        if (e instanceof ConstraintViolationException) {
            return constraintViolationExceptionHandle(e);
        }

        //  处理前端参数反序列化异常
        if (e instanceof HttpMessageNotReadableException) {
            return httpMessageNotReadableExceptionHandle(e);
        }

        //  TODO 自定义异常再此添加

        //  处理 Exception 异常
        if (e instanceof Exception) {
            return exceptionHandle(e);
        }

        return ResponseDataVO.fail(500, "unknown error");
    }

    /**
    * BusinessException 异常处理
    * @param e:  e
    * @return: com.example.junior.vo.ResponseData
    * @Author: Junior
    * @Date: 2023/3/10
    */
    public ResponseDataVO businessExceptionHandle(Throwable e) {
        BusinessException exception = (BusinessException) e;
        return ResponseDataVO.fail(exception.getErrorMsg());
    }

    /**
    * MethodArgumentNotValidException 异常处理
    * @param e:  e
    * @return: com.example.junior.vo.ResponseData
    * @Author: Junior
    * @Date: 2023/3/10
    */
    public ResponseDataVO methodArgumentNotValidExceptionHandle(Throwable e) {
        BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        Map<String, String> errorMap = new HashMap<>(20);
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ResponseDataVO.customize(400, "参数校验异常", errorMap);
    }

    /**
    * ConstraintViolationException 异常处理
    * @param e:  e
    * @return: com.example.junior.vo.ResponseData
    * @Author: Junior
    * @Date: 2023/3/10
    */
    public ResponseDataVO constraintViolationExceptionHandle(Throwable e) {
        List<ConstraintViolation<?>> errorList = new ArrayList<>(((ConstraintViolationException) e).getConstraintViolations());
        Map<String, String> errorMap = new HashMap<>(20);

        errorList.forEach(x -> errorMap.put(x.getPropertyPath().toString(), x.getMessage()));

        return ResponseDataVO.customize(400, "参数校验异常", errorMap);
    }

    /**
    * HttpMessageNotReadableException 异常处理
    * @param e:  e
    * @return: com.example.junior.vo.ResponseData
    * @Author: Junior
    * @Date: 2023/3/10
    */
    public ResponseDataVO httpMessageNotReadableExceptionHandle(Throwable e) {
        return ResponseDataVO.fail("反序列化失败");
    }

    /**
    * Exception 异常处理
    * @param e:  e
    * @return: com.example.junior.vo.ResponseData
    * @Author: Junior
    * @Date: 2023/3/10
    */
    public ResponseDataVO exceptionHandle(Throwable e) {
        Exception exception = (Exception) e;
        return ResponseDataVO.fail(exception.getMessage());
    }

}
