package com.zwb.demo.xc.manage_course.config;

import com.zwb.demo.xc.common.exception.ExceptionCatch;
import com.zwb.demo.xc.common.model.response.CommonCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
/** Created by zwb on 2019/11/1 14:44 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        // 除了CustomException以外的异常类型及对应的错误代码在这里定义,，如果不定义则统一返回固定的错误信息
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}
