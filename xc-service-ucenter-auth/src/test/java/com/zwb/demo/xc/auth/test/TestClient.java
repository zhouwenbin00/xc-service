package com.zwb.demo.xc.auth.test;

import com.sun.jersey.core.util.Base64;
import com.zwb.demo.xc.auth.UcenterAuthApplication;
import com.zwb.demo.xc.common.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/** Created by zwb on 2019/10/30 15:49 */
@SpringBootTest(classes = {UcenterAuthApplication.class})
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired RestTemplate restTemplate;

    @Autowired LoadBalancerClient loadBalancerClient;

    @Test
    public void test1() {
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
        String httpbasic = httpbasic("XcWebApp", "XcWebApp");
        // "Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization", httpbasic);
        // 2、包括：grant_type、username、passowrd
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", "itcast");
        body.add("password", "123");
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity =
                new HttpEntity<>(body, headers);
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
                restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        Map body1 = exchange.getBody();
        System.out.println(body1);
    }

    private String httpbasic(String clientId, String clientSecret) {
        // 将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        // 进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }
}
