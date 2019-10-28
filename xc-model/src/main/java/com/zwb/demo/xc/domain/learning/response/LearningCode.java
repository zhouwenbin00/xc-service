package com.zwb.demo.xc.domain.learning.response;

import com.zwb.demo.xc.common.model.response.ResultCode;
import lombok.ToString;

/** Created by zwb on 2019/10/28 19:54 */
@ToString
public enum LearningCode implements ResultCode {
    TEACHPLAN_IS_NULL(false, 24001, "课程计划不存在！"),
    ;
    // 操作代码
    boolean success;
    // 操作代码
    int code;
    // 提示信息
    String message;

    LearningCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
