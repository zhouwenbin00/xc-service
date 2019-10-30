package com.zwb.demo.xc.api.auth;

import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.ucenter.request.LoginRequest;
import com.zwb.demo.xc.domain.ucenter.response.LoginResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** Created by zwb on 2019/10/30 16:14 */
@Api(value = "用户认证", description = "用户认证接口")
public interface AuthControllerApi {
    @ApiOperation("登录")
    public LoginResult login(LoginRequest loginRequest);

    @ApiOperation("退出")
    public ResponseResult logout();
}
