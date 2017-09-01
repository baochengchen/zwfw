package com.ztesoft.zwfw.domain;


import java.io.Serializable;

/**
 * Created by baoChengchen on 2017/8/14.
 */

public class Message  implements Serializable{
    public String type;
    public String title;
    public String content;
    public String sender;
    public String sendTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

}
