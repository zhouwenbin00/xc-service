package com.zwb.demo.xc.learning.service;

import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.domain.course.TeachplanMediaPub;
import com.zwb.demo.xc.domain.learning.response.GetMediaResult;
import com.zwb.demo.xc.domain.learning.response.LearningCode;
import com.zwb.demo.xc.learning.client.CourseSearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Created by zwb on 2019/10/28 19:49 */
@Service
public class LearningService {

    @Autowired CourseSearchClient courseSearchClient;

    // 获取课程（视频播放地址）
    public GetMediaResult getmedia(String courseId, String teachplanId) {
        // 远程调用搜索服务查询课程计划所对应的的课程媒资信息
        TeachplanMediaPub teachplanMediaPub = courseSearchClient.getmedia(teachplanId);
        if (teachplanMediaPub == null || teachplanMediaPub.getMediaUrl() == null) {
            ExceptionCast.cast(LearningCode.TEACHPLAN_IS_NULL);
        }
        return new GetMediaResult(CommonCode.SUCCESS, teachplanMediaPub.getMediaUrl());
    }
}
