package com.zwb.demo.xc.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Created by zwb on 2019/10/31 16:01 */
// @Component
public class LoginFilterTest extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    // 序号越小越被有限执行
    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() { // 该过滤器需要执行

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        HttpServletRequest request = requestContext.getRequest();
        // 取出头部信息Authorization
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            requestContext.setSendZuulResponse(false);
            // 拒绝访问
            requestContext.setResponseStatusCode(200);
            // 设置响应状态码
            ResponseResult unauthenticated = new ResponseResult(CommonCode.UNAUTHENTICATED);
            String jsonString = JSON.toJSONString(unauthenticated);
            requestContext.setResponseBody(jsonString);
            requestContext.getResponse().setContentType("application/json;charset=UTF‐8");
            return null;
        }
        return null;
    }
}
