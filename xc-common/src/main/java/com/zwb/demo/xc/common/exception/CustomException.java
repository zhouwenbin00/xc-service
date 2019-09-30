package com.zwb.demo.xc.common.exception;

import com.zwb.demo.xc.common.model.response.ResultCode;
import lombok.Data;

/** Created by zwb on 2019/9/30 16:42 */
@Data
public class CustomException extends RuntimeException {

    ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
