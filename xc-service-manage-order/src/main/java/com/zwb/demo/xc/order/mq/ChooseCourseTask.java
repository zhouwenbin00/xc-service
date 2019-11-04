package com.zwb.demo.xc.order.mq;

import com.zwb.demo.xc.domain.task.XcTask;
import com.zwb.demo.xc.order.config.RabbitMQConfig;
import com.zwb.demo.xc.order.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/** Created by zwb on 2019/11/4 14:24 */
@Component
@Slf4j
public class ChooseCourseTask {

    @Autowired TaskService taskService;

    //    @Scheduled(cron = "0/3 * * * * *") // 每隔3s执行一次
    //    @Scheduled(fixedRate = 3000) //任务开始后3s执行下一次
    // 每隔1分钟扫描消息表，向mq发送消息
    @Scheduled(fixedDelay = 3000) // 任务结束后3s执行下一次
    public void sendChoosecourseTask() {
        // 取出当前时间1分钟之前的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE, -1);
        Date time = calendar.getTime();
        List<XcTask> taskList = taskService.findTaskList(time, 1000);
        for (XcTask xcTask : taskList) {
            // 任务id
            String taskId = xcTask.getId();
            // 版本号
            Integer version = xcTask.getVersion();
            // 调用乐观锁方法校验任务是否可以执行
            if (taskService.getTask(taskId, version) > 0) {
                taskService.publish(xcTask, xcTask.getMqExchange(), xcTask.getMqRoutingkey());
                log.info("send choose course task id:{}", taskId);
            }
        }
    }

    /** * 接收选课响应结果 */
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE})
    public void receiveFinishChoosecourseTask(XcTask task) throws IOException {
        log.info("receiveChoosecourseTask...{}", task.getId());
        // 接收到 的消息id
        String id = task.getId();
        // 删除任务，添加历史任务
        taskService.finishTask(id);
    }
}
