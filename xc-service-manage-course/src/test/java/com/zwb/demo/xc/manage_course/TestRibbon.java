package com.zwb.demo.xc.manage_course;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zwb.demo.xc.domain.course.CourseBase;
import com.zwb.demo.xc.domain.course.ext.CourseInfo;
import com.zwb.demo.xc.domain.course.ext.TeachplanNode;
import com.zwb.demo.xc.domain.course.request.CourseListRequest;
import com.zwb.demo.xc.manage_course.dao.CourseBaseRepository;
import com.zwb.demo.xc.manage_course.dao.CourseMapper;
import com.zwb.demo.xc.manage_course.dao.TeachplanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 */
@SpringBootTest(classes = {ManageCourseApplication.class})
@RunWith(SpringRunner.class)
public class TestRibbon {
    @Autowired CourseBaseRepository courseBaseRepository;
    @Autowired RestTemplate restTemplate;
    @Autowired CourseMapper courseMapper;
    @Autowired TeachplanMapper teachplanMapper;

    @Test
    public void testRibbon() {
        // 确定获取的服务名称
        String service = "xc-service-manager-cms";
        for (int i = 0; i < 10; i++) {
            // rabbin从eureka获取服务列表
            ResponseEntity<Map> forEntity =
                    restTemplate.getForEntity(
                            "http://xc-service-manager-cms/cms/page/get/5a754adf6abb500ad05688d9",
                            Map.class);
            Map body = forEntity.getBody();
            System.out.println(body);
        }
    }

    @Test
    public void testCourseBaseRepository() {
        Optional<CourseBase> optional =
                courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }
    }

    @Test
    public void testCourseMapper() {
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);
    }

    @Test
    public void testTeachplanMapper() {
        TeachplanNode teachplanNode =
                teachplanMapper.selectList("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanNode);
    }

    @Test
    public void testPageHelper() {
        PageHelper.startPage(1, 10);
        Page<CourseInfo> courseList = courseMapper.findCourseList(new CourseListRequest());
        List<CourseInfo> result = courseList.getResult();
        System.out.println(result);
    }
}
