package com.zwb.demo.xc.search.controller;

import com.zwb.demo.xc.api.search.EsCourseControllerApi;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.QueryResult;
import com.zwb.demo.xc.domain.course.CoursePub;
import com.zwb.demo.xc.domain.course.TeachplanMediaPub;
import com.zwb.demo.xc.domain.search.CourseSearchParam;
import com.zwb.demo.xc.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** Created by zwb on 2019/10/22 17:44 */
@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {

    @Autowired EsCourseService esCourseService;

    @Override
    @GetMapping(value = "/list/{page}/{size}")
    public QueryResponseResult<CoursePub> list(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            CourseSearchParam courseSearchParam) {
        return esCourseService.list(page, size, courseSearchParam);
    }

    @Override
    @GetMapping("/getall/{id}")
    public Map<String, CoursePub> getall(@PathVariable("id") String id) {
        return esCourseService.getall(id);
    }

    @Override
    @GetMapping(value = "/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
        String[] teachplanIds = new String[] {teachplanId};
        QueryResult<TeachplanMediaPub> result =
                esCourseService.getmedia(teachplanIds).getQueryResult();
        if (result != null) {
            List<TeachplanMediaPub> list = result.getList();
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }
}
