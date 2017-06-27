package com.ziyawang.ziyadaily.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.fragment.CollectFragment;
import com.ziyawang.ziyadaily.fragment.HomeFragment;
import com.ziyawang.ziyadaily.fragment.MyFragment;
import com.ziyawang.ziyadaily.fragment.PublishFragment;
import com.ziyawang.ziyadaily.tools.DownLoadManager;
import com.ziyawang.ziyadaily.tools.NetUtils;
import com.ziyawang.ziyadaily.tools.PermissionsUtil;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.CustomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BenBenActivity {

    private HomeFragment homeFragment ;  //首页Fragment
    private CollectFragment collectFragment ; //收藏Fragment
    private PublishFragment publishFragment ; //发布Fragment
    private MyFragment myFragment ;  //我的Fragment
    private BottomNavigationBar bottomNavigationBar; //底部导航
    private LinearLayout ll_content; //一级Fragment容器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionsUtil.checkAndRequestPermissions(this);
        //检查版本信息
        checkVersion() ;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initViews() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
    }

    @Override
    public void initListeners() {
        homeFragment = new HomeFragment() ;
        myFragment = new MyFragment() ;
        collectFragment = new CollectFragment() ;
        publishFragment = new PublishFragment() ;

        final List<Fragment> listFragment = new ArrayList() ;
        listFragment.add(0 , homeFragment );
        listFragment.add(1 , collectFragment );
        listFragment.add(2 , publishFragment );
        listFragment.add(3 , myFragment );

        //底部导航监听
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {

            @Override
            public void onTabSelected(int position) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (position) {
                    case 0:
                        ActiveFragment(listFragment,transaction,homeFragment);
                        break;
                    case 1:
                        ActiveFragment(listFragment,transaction,publishFragment);
                        break;
                    case 2:
                        ActiveFragment(listFragment,transaction,collectFragment);
                        break;
                    case 3:
                        ActiveFragment(listFragment,transaction,myFragment);
                        break;
                    default:
                        ActiveFragment(listFragment,transaction,homeFragment);
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    @Override
    public void initData() {
//底部导航特性设置
        bottomNavigationBar.setAutoHideEnabled(false);//自动隐藏
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        //底部导航颜色设置
        bottomNavigationBar.setBarBackgroundColor(R.color.bottom_bg); //导航背景颜色
        bottomNavigationBar.setInActiveColor(R.color.bottom_unselected); //未选中时的颜色
        bottomNavigationBar.setActiveColor(R.color.bottom_selected); //选中时的颜色

        //添加底部导航图标和文字
        bottomNavigationBar
//                .addItem(new BottomNavigationItem(R.mipmap.home, R.string.home_home))
//                .addItem(new BottomNavigationItem(R.mipmap.fabu, R.string.home_publish))
//                .addItem(new BottomNavigationItem(R.mipmap.shoucang_54, R.string.home_collect))
//                .addItem(new BottomNavigationItem(R.mipmap.wode, R.string.home_me))
                .addItem(new BottomNavigationItem(R.mipmap.ben01, R.string.home_home))
                .addItem(new BottomNavigationItem(R.mipmap.ben02, R.string.home_publish))
                .addItem(new BottomNavigationItem(R.mipmap.ben03, R.string.home_collect))
                .addItem(new BottomNavigationItem(R.mipmap.ben04, R.string.home_me))
                .initialise();

        //默认选择首页
        SetDefaultFragment();
    }

    private void SetDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.ll_content, homeFragment);
        transaction.commit() ;
    }

    private void ActiveFragment(List<Fragment> listFragment, FragmentTransaction transaction, Fragment fragment) {
        for (int i = 0; i < listFragment.size(); i++) {
            if (listFragment.get(i).isVisible()){
                if (fragment.isAdded()){
                    transaction.hide(listFragment.get(i)).show(fragment) ;
                }else {
                    transaction.hide(listFragment.get(i)).add(R.id.ll_content , fragment) ;
                }
                break;
            }
        }
    }

    private void checkVersion() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.CheckVersion , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("CheckVersion" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        JSONArray data = object.getJSONArray("data");
                        final String UpdateUrl = data.getJSONObject(0).getString("UpdateUrl");
                        String VersionCode = data.getJSONObject(0).getString("VersionCode");
                        String UpdateDes = data.getJSONObject(0).getString("UpdateDes");
                        String UpdateTitle = data.getJSONObject(0).getString("UpdateTitle");

                        int num_web = Integer.parseInt(VersionCode);

                        // 获取packagemanager的实例
                        PackageManager packageManager = getPackageManager();
                        // getPackageName()是你当前类的包名，0代表是获取版本信息
                        PackageInfo packInfo = null;
                        try {
                            packInfo = packageManager.getPackageInfo(getPackageName(),0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        //String version = packInfo.versionName;
                        final int num_local = packInfo.versionCode;

                        if (num_web > num_local){
                            final CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
                            builder.setTitle(UpdateTitle);
                            builder.setMessage(UpdateDes);
                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    int networkType = NetUtils.getNetworkType(MainActivity.this);
                                    switch (networkType){
                                        case NetUtils.NETTYPE_CMNET :
                                        case NetUtils.NETTYPE_CMWAP:
                                            CustomDialog.Builder customDialog = new CustomDialog.Builder(MainActivity.this) ;
                                            customDialog.setTitle("亲爱的用户");
                                            customDialog.setMessage("当前未连接wifi，是否继续下载");
                                            customDialog.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    //下载apk
                                                    final ProgressDialog pd;    //进度条对话框
                                                    pd = new ProgressDialog(MainActivity.this);
                                                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                    pd.setMessage("正在下载更新");
                                                    pd.setCancelable(true);//设置进度条是否可以按退回键取消

                                                    //设置点击进度对话框外的区域对话框不消失
                                                    pd.setCanceledOnTouchOutside(false);
                                                    pd.show();
                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                File file = DownLoadManager.getFileFromServer(Url.FileIP + UpdateUrl, pd);
                                                                sleep(3000);
                                                                installApk(file);
                                                                pd.dismiss(); //结束掉进度条对话框
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }.start();
                                                }
                                            }) ;
                                            customDialog.setNegativeButton("等待wifi", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }) ;
                                            customDialog.create().show();
                                            break;
                                        case NetUtils.NETTYPE_WIFI:

                                            final ProgressDialog pd;    //进度条对话框
                                            pd = new  ProgressDialog(MainActivity.this);
                                            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                            pd.setMessage("正在下载更新");
                                            pd.setCancelable(true);//设置进度条是否可以按退回键取消

                                            //设置点击进度对话框外的区域对话框不消失
                                            pd.setCanceledOnTouchOutside(false) ;
                                            pd.show();
                                            new Thread(){
                                                @Override
                                                public void run() {
                                                    try {
                                                        File file = DownLoadManager.getFileFromServer(Url.FileIP + UpdateUrl, pd);
                                                        sleep(3000);
                                                        installApk(file);
                                                        pd.dismiss(); //结束掉进度条对话框
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }}.start();
                                            break;
                                    }
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }) ;
                            builder.create().show();
                        }

                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(MainActivity.this, "网络连接异常");
            }
        }) ;
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
