package com.zwb.demo.xc.learning.controller;

import com.zwb.demo.xc.api.learning.CourseLearningControllerApi;
import com.zwb.demo.xc.domain.learning.response.GetMediaResult;
import com.zwb.demo.xc.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by zwb on 2019/10/28 17:56 */
@RestController
@RequestMapping("/learning/course")
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired LearningService learningService;

    @Override
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    public GetMediaResult getmedia(
            @PathVariable("courseId") String courseId,
            @PathVariable("teachplanId") String teachplanId) {
        return learningService.getmedia(courseId, teachplanId);
    }
}
