package com.zwb.demo.xc.common.exception;

import com.google.common.collect.ImmutableMap;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.common.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/** Created by zwb on 2019/9/30 16:45 */
@ControllerAdvice // 控制增强器
@Slf4j
public class ExceptionCatch {

    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder =
            ImmutableMap.builder();

    // 捕获异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e) {
        log.error("catch exception:{}", e.getMessage());
        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e) {
        log.error("catch exception:{}", e.getMessage());
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();
        }
        ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if (resultCode != null) {
            return new ResponseResult(resultCode);
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }

    static {
        builder.put(NullPointerException.class, CommonCode.SERVER_ERROR);
    }
}
