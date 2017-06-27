package com.ziyawang.ziyadaily.entity;

/**
 * Created by 牛海丰 on 2017/6/16.
 */

public class MyMessageEntity {

    private String messageId ;
    private String phone ;
    private String picture ;
    private String reply ;
    private String projectId ;
    private String userId ;
    private String created_at ;
    private String updated_at ;
    private String content ;
    private String title ;
    private String time ;

    public MyMessageEntity(){}

    public MyMessageEntity(String messageId, String phone, String picture, String reply, String projectId, String userId, String created_at, String updated_at, String content, String title, String time) {
        super();
        this.messageId = messageId;
        this.phone = phone;
        this.picture = picture;
        this.reply = reply;
        this.projectId = projectId;
        this.userId = userId;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.content = content;
        this.title = title;
        this.time = time;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
