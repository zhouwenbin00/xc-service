package com.zwb.demo.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/** mq入门程序 Created by zwb on 2019/10/9 13:52 */
public class Producer03_routing {

    private static final String QUEUE_EMAIL = "QUEUE_EMAIL";
    private static final String QUEUE_SMS = "QUEUE_SMS";
    private static final String EXCHANGE_ROUNT = "EXCHANGE_ROUNT";

    public static void main(String[] args) throws IOException {
        // 连接工厂,新建连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 生产者和mq建立连接
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        // 设置虚拟机 一个mq服务可以设置多个虚拟机,每个虚拟机相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            // 所有通信都在channel里
            Channel channel = connection.createChannel();
            // 声明交换机
            /*
             * String exchange, String type
             * 名称           类型
             */
            /*
             * DIRECT("direct"),路由
             * FANOUT("fanout"),发布订阅
             * TOPIC("topic"),通配符
             * HEADERS("headers");Header
             */
            channel.exchangeDeclare(EXCHANGE_ROUNT, BuiltinExchangeType.DIRECT);
            // 声明队列
            /* String queue,boolean durable,boolean exclusive, boolean autoDelete,Map<String, Object> arguments
             * 队列名称      持久化            是否独占连接      自动删除            参数
             */
            // 交换机绑定队列
            /*
             * String queue, String exchange, String routingKey
             * 队列名          交换机         路由key
             */
            channel.queueDeclare(QUEUE_EMAIL, true, false, true, null);
            channel.queueDeclare(QUEUE_SMS, true, false, true, null);
            channel.queueBind(QUEUE_EMAIL, EXCHANGE_ROUNT, QUEUE_EMAIL);
            channel.queueBind(QUEUE_SMS, EXCHANGE_ROUNT, QUEUE_SMS);

            /*
             *String exchange, String routingKey, BasicProperties props, byte[] body
             * 交换机          路由key               消息属性              消息内容
             */
            for (int i = 0; i < 5; i++) {
                String message = "helloWorld" + i;
                channel.basicPublish(EXCHANGE_ROUNT, QUEUE_EMAIL, null, message.getBytes());
                System.out.println("send to mq:" + message);
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
