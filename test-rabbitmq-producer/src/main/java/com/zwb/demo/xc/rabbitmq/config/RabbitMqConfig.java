package com.zwb.demo.xc.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Created by zwb on 2019/10/10 20:46 */
@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_TOPIC_INFORM = "exchange_topic_inform";
    public static final String ROUTING_KEY_EMAIL = "inform.email.#";
    public static final String ROUTING_KEY_SMS = "inform.sms.#";
    // 声明交换机
    @Bean(EXCHANGE_TOPIC_INFORM)
    public Exchange EXCHANGE_TOPIC_INFORM() {
        /*
         * durable(持久化)
         */
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC_INFORM).durable(true).build();
    }

    // 声明队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL() {
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS() {
        return new Queue(QUEUE_INFORM_SMS);
    }

    // 队列绑定交换机
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(
            @Qualifier(QUEUE_INFORM_EMAIL) Queue queue,
            @Qualifier(EXCHANGE_TOPIC_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_EMAIL).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_INFORM_SMSL(
            @Qualifier(QUEUE_INFORM_SMS) Queue queue,
            @Qualifier(EXCHANGE_TOPIC_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_SMS).noargs();
    }
}
