package com.ziyawang.ziyadaily.entity;

/**
 * Created by 牛海丰 on 2017/6/16.
 */

public class MyPublishEntity {

    private String type ;
    private String created_at ;
    private String publishTime ;

    public MyPublishEntity (){}

    public MyPublishEntity(String type, String created_at, String publishTime) {
        super();
        this.type = type;
        this.created_at = created_at;
        this.publishTime = publishTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
