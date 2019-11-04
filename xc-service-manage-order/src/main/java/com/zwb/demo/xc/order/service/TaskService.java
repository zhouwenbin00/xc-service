package com.zwb.demo.xc.order.service;

import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.learning.XcLearningCourse;
import com.zwb.demo.xc.domain.learning.response.LearningCode;
import com.zwb.demo.xc.domain.task.XcTask;
import com.zwb.demo.xc.domain.task.XcTaskHis;
import com.zwb.demo.xc.order.dao.XcLearningCourseRepository;
import com.zwb.demo.xc.order.dao.XcTaskHisRepository;
import com.zwb.demo.xc.order.dao.XcTaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/** Created by zwb on 2019/11/4 16:37 */
@Service
public class TaskService {

    @Autowired XcTaskRepository xcTaskRepository;
    @Autowired RabbitTemplate rabbitTemplate;
    @Autowired XcTaskHisRepository xcTaskHisRepository;

    /**
     * 查询前N条任务
     *
     * @param updateTime
     * @param size
     * @return
     */
    public List<XcTask> findTaskList(Date updateTime, int size) {
        Pageable pageable = new PageRequest(0, size);
        Page<XcTask> byUpdateTimeBefore =
                xcTaskRepository.findByUpdateTimeBefore(pageable, updateTime);
        return byUpdateTimeBefore.getContent();
    }

    /**
     * 发布任务
     *
     * @param xcTask
     * @param ex
     * @param routingKey
     */
    @Transactional
    public void publish(XcTask xcTask, String ex, String routingKey) {
        // 查询任务
        Optional<XcTask> taskOptional = xcTaskRepository.findById(xcTask.getId());
        if (taskOptional.isPresent()) {
            XcTask one = taskOptional.get();
            // String exchange, String routingKey, Object object
            rabbitTemplate.convertAndSend(ex, routingKey, xcTask);
            // 更新任务时间为当前时间
            xcTask.setUpdateTime(new Date());
            xcTaskRepository.save(one);
        }
    }

    @Transactional
    public int getTask(String taskId, int version) {
        int i = xcTaskRepository.updateTaskVersion(taskId, version);
        return i;
    }

    // 删除任务
    @Transactional
    public void finishTask(String taskId) {
        Optional<XcTask> taskOptional = xcTaskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            XcTask xcTask = taskOptional.get();
            xcTask.setDeleteTime(new Date());
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask, xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
            xcTaskRepository.delete(xcTask);
        }
    }
}
