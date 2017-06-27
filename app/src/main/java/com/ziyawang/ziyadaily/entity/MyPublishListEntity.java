package com.ziyawang.ziyadaily.entity;

/**
 * Created by 牛海丰 on 2017/6/16.
 */

public class MyPublishListEntity {

    private String describe ;
    private String created_at ;
    private String type ;
    private String status ;
    private String label ;

    public MyPublishListEntity(){}

    public MyPublishListEntity(String describe, String created_at, String type, String status , String label ) {
        super();
        this.describe = describe;
        this.created_at = created_at;
        this.type = type;
        this.status = status;
        this.label = label ;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
