package com.zwb.demo.xc.manage_cms.dao;

import com.zwb.demo.xc.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** Create by zwb on 2019-10-06 19:35 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {

    @Autowired PageService pageService;

    @Test
    public void test() {
        String pageHtml = pageService.getPageHtml("5d99d1eeb84523345dc16b3c");
        System.out.println(pageHtml);
    }
}
