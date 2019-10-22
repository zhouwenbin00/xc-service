package com.zwb.demo.xc.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.QueryResult;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.cms.CmsPage;
import com.zwb.demo.xc.domain.cms.response.CmsPageResult;
import com.zwb.demo.xc.domain.cms.response.CmsPostPageResult;
import com.zwb.demo.xc.domain.course.*;
import com.zwb.demo.xc.domain.course.ext.CourseView;
import com.zwb.demo.xc.domain.course.ext.TeachplanNode;
import com.zwb.demo.xc.domain.course.request.CourseListRequest;
import com.zwb.demo.xc.domain.course.response.AddCourseResult;
import com.zwb.demo.xc.domain.course.response.CoursePublishResult;
import com.zwb.demo.xc.manage_course.client.CmsPageClient;
import com.zwb.demo.xc.manage_course.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/** Create by zwb on 2019-10-13 19:45 */
@Service
@Slf4j
public class CourseService {
    @Resource TeachplanMapper teachplanMapper;
    @Resource TeachplanRepository teachplanRepository;
    @Resource CourseBaseRepository courseBaseRepository;
    @Resource CoursePubRepository coursePubRepository;
    @Resource CourseMapper courseMapper;
    @Resource CourseMarketRepository courseMarketRepository;
    @Resource CoursePicRepository coursePicRepository;
    @Resource CmsPageClient cmsPageClient;

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
    // 添加课程图片
    @Transactional
    public ResponseResult addCoursePic(String id, String pic) {
        Optional<CoursePic> optional = coursePicRepository.findById(id);
        CoursePic coursePic = null;
        if (optional.isPresent()) {
            coursePic = optional.get();
        }
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(id);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic findCoursePic(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        return optional.orElse(null);
    }

    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        long result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    // 查询课程视图，包括课程基本信息，图片，营销，计划
    public CourseView getCourseView(String courseId) {
        CourseView courseView = new CourseView();
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            courseView.setCourseBase(courseBase);
        }
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(courseId);
        if (coursePicOptional.isPresent()) {
            CoursePic coursePic = coursePicOptional.get();
            courseView.setCoursePic(coursePic);
        }
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(courseId);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    @Value("${course‐publish.dataUrlPre}")
    private String publish_dataUrlPre;

    @Value("${course‐publish.pagePhysicalPath}")
    private String publish_page_physicalpath;

    @Value("${course‐publish.pageWebPath}")
    private String publish_page_webpath;

    @Value("${course‐publish.siteId}")
    private String publish_siteId;

    @Value("${course‐publish.templateId}")
    private String publish_templateId;

    @Value("${course‐publish.previewUrl}")
    private String previewUrl;

    public CoursePublishResult preview(String courseid) {
        CourseBase coursebase = this.getCoursebaseById(courseid);
        // 请求cms添加页面
        // 准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageName(courseid + ".html");
        cmsPage.setPageAliase(coursebase.getName());
        cmsPage.setDataUrl(publish_dataUrlPre + courseid);
        // 远程调用cms
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        String pageId = cmsPageResult.getCmsPage().getPageId();
        // 拼装页面url
        String url = previewUrl + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS, url);
    }

    /**
     * 发布课程
     *
     * @param courseid
     * @return
     */
    public CoursePublishResult publish(String courseid) {
        CourseBase coursebase = this.getCoursebaseById(courseid);
        // 调用cms一键发布课程到服务器
        // 准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageName(courseid + ".html");
        cmsPage.setPageAliase(coursebase.getName());
        cmsPage.setDataUrl(publish_dataUrlPre + courseid);
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        // 修改课程的发布状态为已发布
        saveCoursePubState(courseid);
        // 保存课程索引信息
        // 创建CoursePub
        CoursePub coursePub = createCoursePub(courseid);
        saveCoursePub(courseid, coursePub);
        // 缓存课程信息。。
        // 得到页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    private CoursePub saveCoursePub(String courseid, CoursePub coursePub) {
        CoursePub coursePubByd = findCoursePubByd(courseid);
        if (coursePubByd != null) {

        } else {
            coursePubByd = new CoursePub();
        }
        BeanUtils.copyProperties(coursePub, coursePubByd);
        coursePubByd.setId(courseid);
        coursePubByd.setTimestamp(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String data = simpleDateFormat.format(new Date());
        coursePubByd.setPubTime(data);
        CoursePub save = coursePubRepository.save(coursePubByd);
        return save;
    }

    private CoursePub findCoursePubByd(String courseid) {
        Optional<CoursePub> optional = coursePubRepository.findById(courseid);
        return optional.orElse(null);
    }

    /** 创建CoursePub对象 */
    private CoursePub createCoursePub(String courseid) {
        CoursePub coursePub = new CoursePub();
        // 根据id查询course_base
        CourseBase coursebase = this.getCoursebaseById(courseid);
        if (coursebase != null) {
            // 将coursebase拷贝到coursePub
            BeanUtils.copyProperties(coursebase, coursePub);
        }
        // 根据id查询课程营销信息
        CourseMarket courseMarket = getCourseMarketById(courseid);
        if (courseMarket != null) {
            // 将courseMarket拷贝到coursePub
            BeanUtils.copyProperties(courseMarket, coursePub);
        }
        // 根据id查询课程计划信息
        TeachplanNode teachplanList = this.findTeachplanList(courseid);
        String jsonString = JSON.toJSONString(teachplanList);
        coursePub.setTeachplan(jsonString);
        return coursePub;
    }

    // 更改课程状态为已发布
    private CourseBase saveCoursePubState(String courseid) {
        CourseBase coursebase = this.getCoursebaseById(courseid);
        coursebase.setStatus("202002");
        courseBaseRepository.save(coursebase);
        return coursebase;
    }
}
