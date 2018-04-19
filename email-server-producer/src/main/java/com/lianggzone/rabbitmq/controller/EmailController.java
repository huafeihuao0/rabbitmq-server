package com.lianggzone.rabbitmq.controller;

import com.alibaba.fastjson.JSONObject;
import com.lianggzone.rabbitmq.service.EmailService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/***
 *  【邮箱控制器】
 * */
@RestController()
@RequestMapping(value = "/v1/emails")
public class EmailController
{

    @Resource
    private EmailService emailService;

    /**
     * 新增方法
     */
    @RequestMapping(method = RequestMethod.POST)
    public JSONObject add(@RequestBody JSONObject jsonObject) throws Exception
    {
        /* json结构体
        {
            "to":"xxx@163.com",
            "subject":"xxx",
            "text":"<html><head></head><body><h1>邮件测试</h1><p>hello!this is mail test。</p></body></html>"
        }
         */
        emailService.sendEmail(jsonObject.toJSONString());
        return jsonObject;
    }
}