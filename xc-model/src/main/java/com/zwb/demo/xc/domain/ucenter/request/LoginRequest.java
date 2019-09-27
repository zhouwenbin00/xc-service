package com.zwb.demo.xc.domain.ucenter.request;

import com.zwb.demo.xc.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/** Created by admin on 2018/3/5. */
@Data
@ToString
public class LoginRequest extends RequestData {

    String username;
    String password;
    String verifycode;
}
