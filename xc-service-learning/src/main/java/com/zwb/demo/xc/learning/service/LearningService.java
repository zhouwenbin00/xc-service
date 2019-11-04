package com.zwb.demo.xc.learning.service;

import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.course.TeachplanMediaPub;
import com.zwb.demo.xc.domain.learning.XcLearningCourse;
import com.zwb.demo.xc.domain.learning.response.GetMediaResult;
import com.zwb.demo.xc.domain.learning.response.LearningCode;
import com.zwb.demo.xc.domain.task.XcTask;
import com.zwb.demo.xc.domain.task.XcTaskHis;
import com.zwb.demo.xc.learning.client.CourseSearchClient;
import com.zwb.demo.xc.learning.dao.XcLearningCourseRepository;
import com.zwb.demo.xc.learning.dao.XcTaskHisRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/** Created by zwb on 2019/10/28 19:49 */
@Service
public class LearningService {

    @Autowired CourseSearchClient courseSearchClient;
    @Autowired XcTaskHisRepository xcTaskHisRepository;
    @Autowired XcLearningCourseRepository xcLearningCourseRepository;

    // 获取课程（视频播放地址）
    public GetMediaResult getmedia(String courseId, String teachplanId) {
        // 远程调用搜索服务查询课程计划所对应的的课程媒资信息
        TeachplanMediaPub teachplanMediaPub = courseSearchClient.getmedia(teachplanId);
        if (teachplanMediaPub == null || teachplanMediaPub.getMediaUrl() == null) {
            ExceptionCast.cast(LearningCode.TEACHPLAN_IS_NULL);
        }
        return new GetMediaResult(CommonCode.SUCCESS, teachplanMediaPub.getMediaUrl());
    }

    // 完成选课
    @Transactional
    public ResponseResult addcourse(
            String userId,
            String courseId,
            String valid,
            Date startTime,
            Date endTime,
            XcTask xcTask) {
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        if (StringUtils.isEmpty(userId)) {
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_USERISNULL);
        }
        if (xcTask == null || StringUtils.isEmpty(xcTask.getId())) {
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_TASKISNULL);
        }

        Optional<XcTaskHis> optional = xcTaskHisRepository.findById(xcTask.getId());
        if (optional.isPresent()) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        XcLearningCourse xcLearningCourse =
                xcLearningCourseRepository.findXcLearningCourseByUserIdAndCourseId(
                        userId, courseId);
        if (xcLearningCourse == null) {
            // 没有选课记录则添加
            xcLearningCourse = new XcLearningCourse();
            xcLearningCourse.setUserId(userId);
            xcLearningCourse.setCourseId(courseId);
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        } else {
            // 有选课记录则更新日期
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        }
        // 向历史任务表播入记录
        Optional<XcTaskHis> optionalXcTaskHis = xcTaskHisRepository.findById(xcTask.getId());
        if (!optionalXcTaskHis.isPresent()) {
            // 添加历史任务
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask, xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
