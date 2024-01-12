package com.thuwsy.xuetang.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.content.po.MqMessage;
import com.thuwsy.xuetang.content.service.MqMessageService;
import com.thuwsy.xuetang.content.mapper.MqMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴晟宇
* @description 针对表【mq_message】的数据库操作Service实现
* @createDate 2024-01-12 14:44:10
*/
@Service
public class MqMessageServiceImpl extends ServiceImpl<MqMessageMapper, MqMessage>
    implements MqMessageService{

}




