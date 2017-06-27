package com.ziyawang.ziyadaily.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziyadaily.application.MyApplication;

public abstract class BenBenActivity extends AppCompatActivity {
    //声明MyApplication变量
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 定制流程
        setContentView();
        initViews();
        initListeners();
        initData();
        //获得activity的Task路径
        getActivityTask() ;
        //改变通知栏的颜色
        //ChangeNotifyColor.change(R.color.aaa, this);
    }

    private void getActivityTask() {
        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public abstract void setContentView();

    public abstract void initViews();

    public abstract void initListeners();

    public abstract void initData();
}
