package com.zwb.demo.xc.learning.mq;

import com.alibaba.fastjson.JSON;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.task.XcTask;
import com.zwb.demo.xc.learning.config.RabbitMQConfig;
import com.zwb.demo.xc.learning.service.LearningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/** Created by zwb on 2019/11/4 17:19 */
@Component
@Slf4j
public class ChooseCourseTask {
    @Autowired LearningService learningService;
    @Autowired RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE)
    public void receiveChooseTask(XcTask xcTask) {
        log.info("receive choose course task,taskId:{}", xcTask.getId());
        // 接收到 的消息id
        String id = xcTask.getId();
        // 添加选课
        try {
            String requestBody = xcTask.getRequestBody();
            Map map = JSON.parseObject(requestBody, Map.class);
            String userId = (String) map.get("userId");
            String courseId = (String) map.get("courseId");
            String valid = (String) map.get("valid");
            Date startTime = null;
            Date endTime = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            if (map.get("startTime") != null) {
                startTime = dateFormat.parse((String) map.get("startTime"));
            }
            if (map.get("endTime") != null) {
                endTime = dateFormat.parse((String) map.get("endTime"));
            }
            ResponseResult addcourse =
                    learningService.addcourse(userId, courseId, valid, startTime, endTime, xcTask);
            // 选课成功发送响应消息
            if (addcourse.isSuccess()) {
                // 发送响应消息
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,
                        RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY,
                        xcTask);
                log.info("send finish choose course taskId:{}", id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("send finish choose course taskId:{}", id);
        }
    }
}
