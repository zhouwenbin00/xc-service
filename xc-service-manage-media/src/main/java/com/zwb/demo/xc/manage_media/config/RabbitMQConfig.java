package com.zwb.demo.xc.manage_media.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Created by zwb on 2019/10/25 20:48 */
@Configuration
public class RabbitMQConfig {
    public static final String EX_MEDIA_PROCESSTASK = "ex_media_processor"; // 视频处理队列

    @Value("${xc-service-manage-media.mq.queue-media-video-processor}")
    public String queue_media_video_processtask; // 视频处理路由

    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    public String routingkey_media_video;

    /**
     * 交换机配置
     *
     * @return the exchange
     */
    @Bean(EX_MEDIA_PROCESSTASK)
    public Exchange EX_MEDIA_VIDEOTASK() {
        return ExchangeBuilder.directExchange(EX_MEDIA_PROCESSTASK).durable(true).build();
    } // 声明队列
}
