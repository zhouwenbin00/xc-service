package com.zwb.demo.xc.api.search;

import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.domain.course.CoursePub;
import com.zwb.demo.xc.domain.course.TeachplanMediaPub;
import com.zwb.demo.xc.domain.search.CourseSearchParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/** Created by zwb on 2019/10/22 17:40 */
@Api(
        value = "课程搜索",
        description = "课程搜索",
        tags = {"课程搜索"})
public interface EsCourseControllerApi {

    @ApiOperation("课程搜索")
    QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam);

    @ApiOperation("根据id查询课程信息")
    Map<String, CoursePub> getall(String id);

    @ApiOperation("根据课程计划查询媒资信息")
    TeachplanMediaPub getmedia(String teachplanId);
}
