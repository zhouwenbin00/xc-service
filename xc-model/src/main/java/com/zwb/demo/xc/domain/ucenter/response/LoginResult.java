package com.zwb.demo.xc.domain.ucenter.response;

import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.common.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** Created by mrt on 2018/5/21. */
@Data
@ToString
@NoArgsConstructor
public class LoginResult extends ResponseResult {
    public LoginResult(ResultCode resultCode, String token) {
        super(resultCode);
        this.token = token;
    }

    private String token;
}
