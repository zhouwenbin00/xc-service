package com.zwb.demo.xc.manage_cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/** Create by zwb on 2019-10-06 17:42 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateTest {

    @Autowired RestTemplate restTemplate;

    @Test
    public void test() {
        ResponseEntity<Map> forEntity =
                restTemplate.getForEntity(
                        "http://127.0.0.1:31001/cms/config/getmodel/5a791725dd573c3574ee333f",
                        Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);
    }
}
