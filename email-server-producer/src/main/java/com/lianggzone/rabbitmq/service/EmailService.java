package com.lianggzone.rabbitmq.service;


/***
 *  【邮箱服务接口】
 * */
public interface EmailService
{
    /**
     * 发送邮件任务存入消息队列
     *
     * @param message
     * @throws Exception
     */
    void sendEmail(String message) throws Exception;
}