package com.zwb.demo.xc.govern.gateway.filter.service;

import com.zwb.demo.xc.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** Created by zwb on 2019/10/31 16:19 */
@Service
public class AuthService {

    @Autowired StringRedisTemplate stringRedisTemplate;

    public String getTokenFromCookie(HttpServletRequest request) {
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String access_token = cookieMap.get("uid");
        if (StringUtils.isEmpty(access_token)) {
            return null;
        }
        return access_token;
    }

    public long getExpire(String access_token) {
        // token在redis中的key
        String key = "user_token:" + access_token;
        Long expire = stringRedisTemplate.getExpire(key);
        return expire;
    }

    /**
     * 从请求中取出jwt令牌
     *
     * @param request
     * @return
     */
    public String getJwtFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            return null;
        }
        if (!authorization.startsWith("Bearer ")) {
            return null;
        }
        String jwt = authorization.substring(7);
        return jwt;
    }
}
