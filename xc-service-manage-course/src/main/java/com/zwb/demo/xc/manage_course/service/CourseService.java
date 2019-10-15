package com.zwb.demo.xc.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.QueryResult;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.course.CourseBase;
import com.zwb.demo.xc.domain.course.CourseMarket;
import com.zwb.demo.xc.domain.course.Teachplan;
import com.zwb.demo.xc.domain.course.ext.TeachplanNode;
import com.zwb.demo.xc.domain.course.request.CourseListRequest;
import com.zwb.demo.xc.domain.course.response.AddCourseResult;
import com.zwb.demo.xc.manage_course.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/** Create by zwb on 2019-10-13 19:45 */
@Service
@Slf4j
public class CourseService {
    @Resource TeachplanMapper teachplanMapper;
    @Resource TeachplanRepository teachplanRepository;
    @Resource CourseBaseRepository courseBaseRepository;
    @Resource CourseMapper courseMapper;
    @Resource CourseMarketRepository courseMarketRepository;

    // 查询课程计划列表
    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    // 添加课程计划
    public ResponseResult addTeachplan(Teachplan teachplan) {

        if (teachplan == null
                || StringUtils.isEmpty(teachplan.getCourseid())
                || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALLD_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        // 要处理parentId
        if (StringUtils.isEmpty(parentid)) {
            parentid = this.getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan parentNode = optional.get();
        Teachplan teachplanNew = new Teachplan();
        // 将页面提交的teachplan信息拷到teachplanNew对象中
        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);

        // 根据父节点设置
        if (parentNode.getGrade().equals("1")) {

            teachplanNew.setGrade("2");
        } else {
            teachplanNew.setGrade("3");
        }
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /** 查询课程的根节点 */
    private String getTeachplanRoot(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        List<Teachplan> teachplanList =
                teachplanRepository.findByCourseidAndAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.isEmpty()) {
            // 查询不到，自动添加根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        return teachplanList.get(0).getId();
    }

    public QueryResponseResult findCourseList(
            int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        if (page <= 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 20;
        }
        PageHelper.startPage(page, size);
        Page<CourseBase> courseList = courseMapper.findCourseList(courseListRequest);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(courseList.getResult());
        queryResult.setTotal(courseList.getTotal());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    // 添加课程提交
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        // 课程状态默认为未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }

    public CourseBase getCoursebaseById(String courseid) {
        return courseBaseRepository.findById(courseid).orElse(null);
    }

    @Transactional
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        CourseBase one = this.getCoursebaseById(id);
        if (one == null) {
            // 抛出异常..
            ExceptionCast.cast(CommonCode.INVALLD_PARAM);
        }
        // 修改课程信息
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());
        CourseBase save = courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CourseMarket getCourseMarketById(String courseId) {
        return courseMarketRepository.findById(courseId).orElse(null);
    }

    @Transactional
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = this.getCourseMarketById(id);
        if (one == null) {
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
            one.setId(id);
            courseMarketRepository.save(one);
        } else {
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());
            // 课程有效期，开始时间
            one.setEndTime(courseMarket.getEndTime());
            // 课程有效期，结束时间
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketRepository.save(one);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
