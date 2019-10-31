package com.zwb.demo.xc.auth.controller;

import com.zwb.demo.xc.api.auth.AuthControllerApi;
import com.zwb.demo.xc.auth.service.AuthService;
import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.ucenter.ext.AuthToken;
import com.zwb.demo.xc.domain.ucenter.request.LoginRequest;
import com.zwb.demo.xc.domain.ucenter.response.AuthCode;
import com.zwb.demo.xc.domain.ucenter.response.JwtResult;
import com.zwb.demo.xc.domain.ucenter.response.LoginResult;
import com.zwb.demo.xc.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/** Created by zwb on 2019/10/30 16:15 */
@RestController
public class AuthController implements AuthControllerApi {
    @Autowired AuthService authService;

    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.clientSecret}")
    String clientSecret;

    @Value("${auth.cookieDomain}")
    String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest) {
        // 校验账号是否输入
        if (loginRequest == null || StringUtils.isBlank(loginRequest.getUsername())) {
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        // 校验密码是否输入
        if (StringUtils.isBlank(loginRequest.getPassword())) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        AuthToken authToken =
                authService.login(
                        loginRequest.getUsername(),
                        loginRequest.getPassword(),
                        clientId,
                        clientSecret);
        // 将令牌写入cookie
        // 访问token
        String access_token = authToken.getAccess_token();
        // 将访问令牌存储到cookie
        saveCookie(access_token);
        return new LoginResult(CommonCode.SUCCESS, access_token);
    }

    // 将令牌保存到cookie
    private void saveCookie(String token) {
        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getResponse(); // 添加cookie 认证令牌，最后一个参数设置为false，表示允许浏览器获取
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, cookieMaxAge, false);
    }

    @Override
    @PostMapping("/userlogout")
    public ResponseResult logout() {
        // 取出cookie的身份令牌
        String uid = getTokenForCookie();
        if (uid == null) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        // 删除redis的token
        boolean b = authService.delToken(uid);
        if (!b) {
            ExceptionCast.cast(AuthCode.AUTH_LOGOUT_FAIL);
        }
        // 清除cookie
        clearCookie(uid);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 删除cookie
     *
     * @param token
     */
    private void clearCookie(String token) {
        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false);
    }

    @Override
    @GetMapping("/userjwt")
    public JwtResult userjwt() {
        // 取出cookie的身份令牌
        String access_token = getTokenForCookie();
        if (access_token == null) {
            return new JwtResult(CommonCode.FAIL, null);
        }
        // 用身份令牌到redis查询jwt
        AuthToken userToken = authService.getUserToken(access_token);
        if (userToken == null) {
            return new JwtResult(CommonCode.FAIL, null);
        }
        // 将Jwt令牌返回给用户
        return new JwtResult(CommonCode.SUCCESS, userToken.getJwt_token());
    }

    /**
     * 获取cookie中的身份令牌
     *
     * @return
     */
    private String getTokenForCookie() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        if (cookieMap != null && cookieMap.get("uid") != null) {
            return cookieMap.get("uid");
        }
        return null;
    }
}
