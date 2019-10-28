package com.zwb.demo.xc.learning.client;

import com.zwb.demo.xc.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** Created by zwb on 2019/10/28 19:50 */
@FeignClient(value = "xc-service-search")
public interface CourseSearchClient {

    @GetMapping(value = "/search/course/getmedia/{teachplanId}")
    TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);
}
