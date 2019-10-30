package com.zwb.demo.xc.auth.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Created by zwb on 2019/10/30 14:32 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {
        // 定义key
        String key = "user_token:7375aacc-7de8-4989-a368-e371bad9884a";
        // 定义value
        Map<String, String> map = new HashMap<>();
        map.put(
                "jwt",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Inp3YiIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3MjM2NDgzMiwianRpIjoiNzM3NWFhY2MtN2RlOC00OTg5LWEzNjgtZTM3MWJhZDk4ODRhIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.OsoXotKNHUg7uHgYyAeL-jKHA4RTIoRLE9byhs2Avst96V8zoyizNRFEaTkNZcO6oFjsuzdeDqzCbw6mBDZvgD6X3yrxVqzCFgpPEYW4xVhxq9kGKUOYSc90jy6MzPh4Vys-XtEPZkXIJVDlVLbC3fsIoPMvJ5C2GooC5pPfQxm75xMyJqEndeSEly66rwKPtylNezs-TBcO6-otMMTH_6Zyo7HsxyHs35A-zMTFalEr4nHiN8jufRyDPrTKbYY8HA3dJm5if5uqsDtLbE5CeYKxvOMFoKPlZz35TixIRRPSGWRfcSINvx1qgsAGzJCb2i2nTVeWSVkzw0bVj5v9mw");
        map.put(
                "refresh_token",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Inp3YiIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiI3Mzc1YWFjYy03ZGU4LTQ5ODktYTM2OC1lMzcxYmFkOTg4NGEiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3MjM2NDgzMiwianRpIjoiMjM1ZjFmOTctNTk3NC00MWRhLWE2ZmYtNjY0MzAwNGFmZDRiIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.FM-xGNPbC5G_ueNK15JwCiPE5fT_D4LOUdaX0-rRsXUYKquT6_A_E1fmPbJyXTmh0-OGVJPNXzajPi6A0H7_7v46UfpcSQLwADuit_lBqD_lf0La4-Cj5Tks2heodnYgLx-sTSTlQGOxBbG7D90VGkamzcKVP0rWcqxnzprls_xPWFBo7adh7Wk2t4wpj7BJp14O4H_RF8TNWadk6xdtF7RW4oO1Z4zY0paVJ2nITYn4GYUuc3I3qZ2lfzZVhqf2cdaa9kXg-KkomHnj5ZHUFgB7r-OQ-Kqfw7RHqgOrWwIyw_zzFpO9XFV8OJTyXogQ1bi8SKA0YC95nCapSlkDfA");
        String jsonString = JSON.toJSONString(map);
        // 校验key是否存在
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        System.out.println(expire);
        // 存储数据
        stringRedisTemplate.boundValueOps(key).set(jsonString, 30, TimeUnit.SECONDS);
        // 获取数据
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);
    }
}
