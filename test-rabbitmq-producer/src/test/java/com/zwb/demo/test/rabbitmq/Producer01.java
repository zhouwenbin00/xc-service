package com.zwb.demo.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/** mq入门程序 Created by zwb on 2019/10/9 13:52 */
public class Producer01 {

    private static final String QUEUE = "helloWorld1";

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
            // 声明队列
            /* String queue,boolean durable,boolean exclusive, boolean autoDelete,Map<String, Object> arguments
             * 队列名称      持久化            是否独占连接      自动删除            参数
             */
            channel.queueDeclare(QUEUE, true, false, true, null);
            /*
             *String exchange, String routingKey, BasicProperties props, byte[] body
             * 交换机          路由key               消息属性              消息内容
             */
            String message = "helloWorld";
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("send to mq:" + message);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
