package com.ziyawang.ziyadaily.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by 牛海丰 on 2016/7/19.
 */
public class MyApplication extends Application {

    //activity Task
    private static List<Activity> activities = new ArrayList<Activity>();

    //add activity
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    //cancelActivity
    public void cancelActivity(Activity activity){
        activities.remove(activity) ;
    }

    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activities.contains(activity)) {
                activities.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     */
    public static void finishSingleActivityByClass(Class cls) {
        Activity tempActivity = null;
        for (Activity activity : activities) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }

        finishSingleActivity(tempActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : activities) {
            activity.finish();
        }
        System.exit(0);
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
