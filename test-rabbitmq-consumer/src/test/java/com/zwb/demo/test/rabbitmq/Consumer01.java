package com.zwb.demo.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/** Created by zwb on 2019/10/9 14:09 */
public class Consumer01 {

    private static final String QUEUE = "helloWorld1";

    public static void main(String[] args) {
        // 连接工厂,新建连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 生产者和mq建立连接
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        // 设置虚拟机 一个mq服务可以设置多个虚拟机,每个虚拟机相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        try {
            Connection connection = connectionFactory.newConnection();
            // 所有通信都在channel里
            Channel channel = connection.createChannel();
            // 声明队列
            /* String queue,boolean durable,boolean exclusive, boolean autoDelete,Map<String, Object> arguments
             * 队列名称      持久化            是否独占连接      自动删除            参数
             */
            channel.queueDeclare(QUEUE, true, false, true, null);
            // 监听队列
            /*
             * String queue, boolean autoAck, Consumer callback
             * 名称            自动回复        消费方法
             */
            DefaultConsumer defaultConsumer =
                    new DefaultConsumer(channel) {
                        /*当街收到消息，此方法被调用*/
                        @Override
                        public void handleDelivery(
                                String consumerTag,
                                Envelope envelope,
                                AMQP.BasicProperties properties,
                                byte[] body)
                                throws IOException {
                            // 交换机
                            String exchange = envelope.getExchange();
                            // 消息id
                            long deliveryTag = envelope.getDeliveryTag();
                            String message = new String(body, "utf-8");
                            System.out.println("rec from mq :" + message);
                        }
                    };
            channel.basicConsume(QUEUE, true, defaultConsumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
        }
    }
}