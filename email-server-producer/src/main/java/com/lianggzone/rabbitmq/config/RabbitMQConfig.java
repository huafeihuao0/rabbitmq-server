package com.lianggzone.rabbitmq.config;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

//配置类
@Configuration
//包扫描
@ComponentScan(basePackages = {"com.lianggzone.rabbitmq"})
//属性资源
@PropertySource(value = {"classpath:application.properties"})
public class RabbitMQConfig
{
    //环境变量
    @Autowired
    private Environment env;

    /***
     *  注册连接工厂
     * */
    @Bean
    public ConnectionFactory connectionFactory() throws Exception
    {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(env.getProperty("mq.host")
                                     .trim());
        connectionFactory.setPort(Integer.parseInt(env.getProperty("mq.port")
                                                      .trim()));
        connectionFactory.setVirtualHost(env.getProperty("mq.vhost")
                                            .trim());
        connectionFactory.setUsername(env.getProperty("mq.username")
                                         .trim());
        connectionFactory.setPassword(env.getProperty("mq.password")
                                         .trim());
        return connectionFactory;
    }

    /***
     *  注册带缓冲的连接工厂
     * */
    @Bean
    public CachingConnectionFactory cachingConnectionFactory() throws Exception
    {
        return new CachingConnectionFactory(connectionFactory());
    }

    /***
     *  注册操作模板
     * */
    @Bean
    public RabbitTemplate rabbitTemplate() throws Exception
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory());
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    /***
     *  注册管理员
     * */
    @Bean
    public AmqpAdmin amqpAdmin() throws Exception
    {
        return new RabbitAdmin(cachingConnectionFactory());
    }

    /***
     *  注册队列
     * */
    @Bean
    Queue queue()
    {
        String name = env.getProperty("mq.queue")
                         .trim();
        // 是否持久化
        boolean durable = StringUtils.isNotBlank(env.getProperty("mq.queue.durable")
                                                    .trim()) ?
                Boolean.valueOf(env.getProperty("mq.queue.durable")
                                   .trim()) : true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = StringUtils.isNotBlank(env.getProperty("mq.queue.exclusive")
                                                      .trim()) ?
                Boolean.valueOf(env.getProperty("mq.queue.exclusive")
                                   .trim()) : false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = StringUtils.isNotBlank(env.getProperty("mq.queue.autoDelete")
                                                       .trim()) ?
                Boolean.valueOf(env.getProperty("mq.queue.autoDelete")
                                   .trim()) : false;
        return new Queue(name, durable, exclusive, autoDelete);
    }

    /***
     *  注册交换器
     * */
    @Bean
    TopicExchange exchange()
    {
        String name = env.getProperty("mq.exchange")
                         .trim();
        // 是否持久化
        boolean durable = StringUtils.isNotBlank(env.getProperty("mq.exchange.durable")
                                                    .trim()) ?
                Boolean.valueOf(env.getProperty("mq.exchange.durable")
                                   .trim()) : true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = StringUtils.isNotBlank(env.getProperty("mq.exchange.autoDelete")
                                                       .trim()) ?
                Boolean.valueOf(env.getProperty("mq.exchange.autoDelete")
                                   .trim()) : false;
        return new TopicExchange(name, durable, autoDelete);
    }

    /***
     *  注册绑定器
     * */
    @Bean
    Binding binding()
    {
        String routekey = env.getProperty("mq.routekey")
                             .trim();
        return BindingBuilder.bind(queue())
                             .to(exchange())
                             .with(routekey);
    }
}
