package com.zwb.demo.xc.auth.service;

import com.alibaba.fastjson.JSON;
import com.zwb.demo.xc.common.client.XcServiceList;
import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.domain.ucenter.ext.AuthToken;
import com.zwb.demo.xc.domain.ucenter.request.LoginRequest;
import com.zwb.demo.xc.domain.ucenter.response.AuthCode;
import com.zwb.demo.xc.domain.ucenter.response.LoginResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Created by zwb on 2019/10/30 16:15 */
@Service
public class AuthService {

    @Autowired RestTemplate restTemplate;

    @Autowired LoadBalancerClient loadBalancerClient;
    @Autowired StringRedisTemplate stringRedisTemplate;

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    // 认证方法
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        // 请求spring security请求令牌
        AuthToken authToken = applyAuthToken(username, password, clientId, clientSecret);
        // 将令牌存储到redis
        String access_token = authToken.getAccess_token();
        String jsonString = JSON.toJSONString(authToken);
        boolean saveTokenResult = saveToken(access_token, jsonString, tokenValiditySeconds);
        if (!saveTokenResult) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }
        return authToken;
    }

    private boolean saveToken(String access_token, String content, long ttl) {
        String key = "user_token:" + access_token;
        stringRedisTemplate.boundValueOps(key).set(content, ttl, TimeUnit.SECONDS);
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire > 0;
    }

    // 申请令牌
    private AuthToken applyAuthToken(
            String username, String password, String clientId, String clientSecret) {
        // 从erurka中获取认证服务的地址
        ServiceInstance serviceInstance =
                loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        // 此地址是http://ip:port
        URI uri = serviceInstance.getUri();
        // 令牌申请的地址http://localhost:40400/auth/oauth/token
        String authUrl = uri + "auth/oauth/token";
        // URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType
        // url就是 申请令牌的url /oauth/token
        // method http的方法类型
        // requestEntity请求内容
        // responseType，将响应的结果生成的类型
        // 请求的内容分两部分
        // 1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String httpbasic = httpbasic(clientId, clientSecret);
        // "Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization", httpbasic);
        // 2、包括：grant_type、username、passowrd
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity =
                new HttpEntity<>(body, headers);
        Map map = null;
        try {
            // 指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
            restTemplate.setErrorHandler(
                    new DefaultResponseErrorHandler() {
                        @Override
                        public void handleError(ClientHttpResponse response) throws IOException {
                            // 当响应的值为400或401时候也要正常响应，不要抛出异常
                            if (response.getRawStatusCode() != 400
                                    && response.getRawStatusCode() != 401) {
                                super.handleError(response);
                            }
                        }
                    });
            // 远程调用申请令牌
            ResponseEntity<Map> exchange =
                    restTemplate.exchange(
                            authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
            map = exchange.getBody();
        } catch (Exception e) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        if (map == null
                || map.get("access_token") == null
                || map.get("refresh_token") == null
                || map.get("jti") == null) { // jti是jwt令牌的唯一标识作为用户身份令牌
            String error_description = (String) map.get("error_description");
            if (StringUtils.isNotEmpty(error_description)) {
                if (error_description.equals("坏的凭证")) {
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                } else if (error_description.contains("UserDetailsService returned null")) {
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }
            }
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }

        AuthToken authToken = new AuthToken();
        authToken.setJwt_token((String) map.get("access_token")); // 用户令牌
        authToken.setRefresh_token((String) map.get("refresh_token")); // 刷新令牌
        authToken.setAccess_token((String) map.get("jti")); // jwt令牌
        return authToken;
    }

    private String httpbasic(String clientId, String clientSecret) {
        // 将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        // 进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }

    /**
     * 从redis查询令牌
     *
     * @param token
     * @return
     */
    public AuthToken getUserToken(String token) {
        String userToken = "user_token:" + token;
        String userTokenString = stringRedisTemplate.opsForValue().get(userToken);
        if (userTokenString == null) {
            return null;
        }
        return JSON.parseObject(userTokenString, AuthToken.class);
    }

    /**
     * 删除令牌
     *
     * @param access_token
     * @return
     */
    public boolean delToken(String access_token) {
        String name = "user_token:" + access_token;
        stringRedisTemplate.delete(name);
        Long expire = stringRedisTemplate.getExpire(access_token, TimeUnit.SECONDS);
        return expire < 0;
    }
}
