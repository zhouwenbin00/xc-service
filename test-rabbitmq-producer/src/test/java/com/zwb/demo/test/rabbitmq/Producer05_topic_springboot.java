package com.zwb.demo.test.rabbitmq;

import com.zwb.demo.xc.rabbitmq.TestRabbitMqApplication;
import com.zwb.demo.xc.rabbitmq.config.RabbitMqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** Created by zwb on 2019/10/10 21:15 */
@SpringBootTest(classes = TestRabbitMqApplication.class)
@RunWith(SpringRunner.class)
public class Producer05_topic_springboot {

    @Autowired RabbitTemplate rabbitTemplate;

    @Test
    public void testSendEmail() {
        String message = "Send email message";
        /*
         *String exchange,String routingKey, Object message
         */
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_TOPIC_INFORM, RabbitMqConfig.ROUTING_KEY_EMAIL, message);
    }
}
