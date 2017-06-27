package com.ziyawang.ziyadaily.entity;

/**
 * Created by 牛海丰 on 2017/6/16.
 */

public class InfoNoticeEntity {

    private String systemId ;
    private String content ;
    private String created_at ;
    private String updated_at ;
    private String userId ;

    public InfoNoticeEntity (){}

    public InfoNoticeEntity(String systemId, String content, String created_at, String updated_at, String userId) {
        super();
        this.systemId = systemId;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.userId = userId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
