package com.zwb.demo.xc.rabbitmq.mq;

import com.zwb.demo.xc.rabbitmq.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Created by zwb on 2019/10/10 21:22 */
@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFORM_EMAIL})
    public void send_email(String body) {
        System.out.println(body);
    }
}
