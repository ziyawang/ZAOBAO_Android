package com.ziyawang.ziyadaily.entity;

/**
 * Created by 牛海丰 on 2017/6/13.
 */

public class HomeEntity {

    private String projectId ;
    private String title ;
    private String content ;
    private String type ;
    private String label ;
    private String describe ;
    private String created_at ;
    private String updated_at ;
    private String status ;
    private String phoneNumber ;

    public HomeEntity() {}

    public HomeEntity(String projectId, String title, String content, String type, String label, String describe, String created_at, String updated_at, String status , String phoneNumber) {
        super();
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.label = label;
        this.describe = describe;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.phoneNumber = phoneNumber ;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
