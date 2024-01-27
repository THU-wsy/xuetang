package com.thuwsy.xuetang.message.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.message.feign.CourseIndexFeignService;
import com.thuwsy.xuetang.message.feign.CoursePublishFeignService;
import com.thuwsy.xuetang.message.po.CourseIndex;
import com.thuwsy.xuetang.message.po.CoursePublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ClassName: MessageConsumer
 * Package: com.thuwsy.xuetang.message.consumer
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/24 14:23
 * @Version 1.0
 */
@Component
@Slf4j
public class MessageConsumer {
    @Autowired
    private CoursePublishFeignService coursePublishFeignService;

    @Autowired
    private CourseIndexFeignService courseIndexFeignService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queues = "queue.xuetang.minio")
    public void handleMinioMessage(Message message, Channel channel) throws IOException {
        // 1. 获取消息的投递序号
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 2. 获取消息中所封装的实体类
            String json = new String(message.getBody());
            CoursePublish coursePublish = JSONObject.parseObject(json, CoursePublish.class);

            // 3. 远程调用CoursePublishController中的API生成静态页面，并上传至minio
            coursePublishFeignService.coursePublishToMinio(coursePublish.getId());

            // 4. 成功上传minio后，返回ack（不采用累积确认）
            channel.basicAck(deliveryTag, false);
            log.info("生成静态页面，并上传到minio成功");
        } catch (Exception e) {
            // 5. 出现异常，则返回nack，并重新入队
            channel.basicReject(deliveryTag, true);
        }
    }

    @RabbitListener(queues = "queue.xuetang.es")
    public void handleEsMessage(Message message, Channel channel) throws IOException {
        // 1. 获取消息的投递序号
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 2. 获取消息中所封装的实体类
            String json = new String(message.getBody());
            CoursePublish coursePublish = JSONObject.parseObject(json, CoursePublish.class);

            // 3. 拷贝至课程索引对象
            CourseIndex courseIndex = new CourseIndex();
            BeanUtils.copyProperties(coursePublish, courseIndex);

            // 4. 远程调用CourseIndexController中的API，向ES添加课程文档
            Boolean add = courseIndexFeignService.add(courseIndex);

            // 5. 成功添加至es后，返回ack（不采用累积确认）
            if (add) {
                channel.basicAck(deliveryTag, false);
                log.info("成功添加课程到Elasticsearch的索引库");
            } else {
                XueTangException.cast("添加课程到es失败");
            }
        } catch (Exception e) {
            // 5. 出现异常，则返回nack，并重新入队
            channel.basicReject(deliveryTag, true);
        }
    }

    @RabbitListener(queues = "queue.xuetang.redis")
    public void handleRedisMessage(Message message, Channel channel) throws IOException {
        // 1. 获取消息的投递序号
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 2. 获取消息中所封装的实体类
            String json = new String(message.getBody());
            CoursePublish coursePublish = JSONObject.parseObject(json, CoursePublish.class);

            // 3. 缓存到Redis中
            String key = "courseId:" + coursePublish.getId();
            stringRedisTemplate.opsForValue().set(key, json);

            // 4. 成功缓存到Redis后，返回ack（不采用累积确认）
            channel.basicAck(deliveryTag, false);
            log.info("课程发布信息成功缓存到Redis");
        } catch (Exception e) {
            // 5. 出现异常，则返回nack，并重新入队
            channel.basicReject(deliveryTag, true);
        }
    }
}
