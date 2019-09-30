package com.zwb.demo.xc.common.exception;

import com.zwb.demo.xc.common.model.response.ResultCode;

/** Created by zwb on 2019/9/30 16:44 */
public class ExceptionCast {

    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
