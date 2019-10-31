package com.zwb.demo.xc.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.govern.gateway.filter.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Created by zwb on 2019/10/31 16:15 */
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired AuthService authService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 请求对象
        HttpServletRequest request = requestContext.getRequest();
        // 查询身份令牌
        String access_token = authService.getTokenFromCookie(request);
        if (access_token == null) {
            // 拒绝访问
            access_denied();
        }
        // 从redis中校验身份令牌是否过期
        long expire = authService.getExpire(access_token);
        if (expire <= 0) {
            // 拒绝访问
            access_denied();
        }
        // 查询jwt令牌
        String jwt = authService.getJwtFromHeader(request);
        if (jwt == null) {
            // 拒绝访问
            access_denied();
        }
        return null;
    }

    // 拒绝访问
    private void access_denied() { // 上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.setSendZuulResponse(false); // 拒绝访问
        // 设置响应内容
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String responseResultString = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(responseResultString);
        // 设置状态码
        requestContext.setResponseStatusCode(200);
        HttpServletResponse response = requestContext.getResponse();
        response.setContentType("application/json;charset=utf‐8");
    }
}
