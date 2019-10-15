package com.zwb.demo.xc.api.course;

import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.course.CourseBase;
import com.zwb.demo.xc.domain.course.CourseMarket;
import com.zwb.demo.xc.domain.course.Teachplan;
import com.zwb.demo.xc.domain.course.ext.CourseInfo;
import com.zwb.demo.xc.domain.course.ext.TeachplanNode;
import com.zwb.demo.xc.domain.course.request.CourseListRequest;
import com.zwb.demo.xc.domain.course.response.AddCourseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** Create by zwb on 2019-10-13 18:22 */
@Api(value = "课程管理接口", description = "课程管理接口,提供增删改查")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程基础信息")
    AddCourseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("获取课程基础信息")
    CourseBase getCoursebaseById(String courseid);

    @ApiOperation("更新课程基础信息")
    ResponseResult updateCourseBase(String id, CourseBase courseBase);

    @ApiOperation("获取课程营销信息")
    CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("更新课程营销信息")
    ResponseResult updateCourseMarket(String id, CourseMarket courseMarket);
}
