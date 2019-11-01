package com.zwb.demo.xc.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static org.springframework.web.context.request.RequestContextHolder.*;

/** feigin拦截器 Created by zwb on 2019/11/1 15:20 */
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 取出header
        // 使用RequestContextHolder工具获取request相关变量
        ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
        if (attributes != null) {
            // 取出request
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    if (name.equals("authorization")) {
                        // System.out.println("name="+name+"values="+values);
                        requestTemplate.header(name, values);
                    }
                }
            }
        }
        // 将jwt向下传递
    }
}
