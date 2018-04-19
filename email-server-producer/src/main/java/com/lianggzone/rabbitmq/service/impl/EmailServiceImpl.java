package com.lianggzone.rabbitmq.service.impl;

import com.lianggzone.rabbitmq.service.EmailService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/***
 *  【邮箱服务接口实现】
 * */
@Service
public class EmailServiceImpl
        implements EmailService
{
    private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Resource(name = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.exchange}")
    private String exchange;

    @Value("${mq.routekey}")
    private String routeKey;

    @Override
    public void sendEmail(String message) throws Exception
    {
        try
        {
            //使用RabbitMQ发送消息到队列
            rabbitTemplate.convertAndSend(exchange, routeKey, message);
        } catch (Exception e)
        {
            logger.error("EmailServiceImpl.sendEmail", ExceptionUtils.getMessage(e));
        }
    }
}
