package com.hrm.common.handler;

import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 自定义公共异常处理器
 * @Author LZL
 * @Date 2022/1/12-12:54
 */
@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, HttpServletResponse response,Exception e){
        Result result = new Result(ResultCode.SERVER_ERROR);
        return result;
    }
}
