package com.zwb.demo.xc.domain.course.ext;

import com.zwb.demo.xc.domain.course.CourseBase;
import com.zwb.demo.xc.domain.course.CourseMarket;
import com.zwb.demo.xc.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/** Created by zwb on 2019/10/18 10:42 */
@Data
@ToString
@NoArgsConstructor
public class CourseView implements Serializable {

    private CourseBase courseBase; // 基础信息
    private CoursePic coursePic; // 课程营销
    private CourseMarket courseMarket; // 课程图片
    private TeachplanNode teachplanNode; // 教学计划
}
