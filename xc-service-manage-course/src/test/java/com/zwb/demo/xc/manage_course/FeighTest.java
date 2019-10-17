package com.zwb.demo.xc.manage_course;

import com.zwb.demo.xc.domain.cms.CmsPage;
import com.zwb.demo.xc.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** Created by zwb on 2019/10/17 17:01 */
@SpringBootTest(classes = {ManageCourseApplication.class})
@RunWith(SpringRunner.class)
public class FeighTest {

    @Autowired CmsPageClient cmsPageClient;

    @Test
    public void test() {
        CmsPage cmsPageById = cmsPageClient.findCmsPageById("5a754adf6abb500ad05688d9");
        System.out.println(cmsPageById);
    }
}
