package com.lianggzone.rabbitmq.component;

import com.alibaba.fastjson.JSONObject;
import com.lianggzone.rabbitmq.model.MailMessageModel;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/***
 *  【发送者】
 * */
@Component("mailMessageListenerAdapter")
public class MailMessageListenerAdapter
        extends MessageListenerAdapter  //集成消息监听器适配器
{

    @Resource
    private JavaMailSender mailSender;

    @Value("${mail.username}")
    private String mailUsername;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception
    {
        System.out.println(message.getMessageProperties()
                                  .getConsumerQueue());
        try
        {
            // 【解析RabbitMQ消息体】
            String messageBody = new String(message.getBody());
            MailMessageModel mailMessageModel = JSONObject.toJavaObject(JSONObject.parseObject(messageBody), MailMessageModel.class);

            // 发送邮件
            String to = mailMessageModel.getTo();
            String subject = mailMessageModel.getSubject();
            String text = mailMessageModel.getText();
            sendHtmlMail(to, subject, text);

            // 【手动ACK】
            channel.basicAck(message.getMessageProperties()//消息属性
                                    .getDeliveryTag(), //分发标记
                    false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 发送邮件
     *
     * @param to
     * @param subject
     * @param text
     * @throws Exception
     */
    private void sendHtmlMail(String to, String subject, String text) throws Exception
    {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(mailUsername);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);
        // 发送邮件
        mailSender.send(mimeMessage);
    }
}