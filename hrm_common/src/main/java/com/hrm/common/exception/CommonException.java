package com.hrm.common.exception;

import com.hrm.common.entity.ResultCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/10-6:30
 */
@AllArgsConstructor
@NoArgsConstructor
public class CommonException extends Exception{
    private ResultCode resultCode;
}
