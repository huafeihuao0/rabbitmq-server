package com.lianggzone.rabbitmq.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/***
 *  【fastjson模型注解】
 * */
@Data
public class MailMessageModel
{
    @JSONField(name = "from")
    private String from;

    @JSONField(name = "to")
    private String to;

    @JSONField(name = "subject")
    private String subject;

    @JSONField(name = "text")
    private String text;

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Email{from:")
          .append(this.from)
          .append(", ");
        sb.append("to:")
          .append(this.to)
          .append(", ");
        sb.append("subject:")
          .append(this.subject)
          .append(", ");
        sb.append("text:")
          .append(this.text)
          .append("}");
        return sb.toString();
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}