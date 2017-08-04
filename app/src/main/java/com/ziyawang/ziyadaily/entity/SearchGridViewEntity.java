package com.ziyawang.ziyadaily.entity;

/**
 * Created by 牛海丰 on 2017/8/2.
 */

public class SearchGridViewEntity {

    private String title ;

    public SearchGridViewEntity(){}

    public SearchGridViewEntity(String title) {
        super();
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
