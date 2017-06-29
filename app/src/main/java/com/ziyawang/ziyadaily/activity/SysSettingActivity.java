package com.ziyawang.ziyadaily.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wx.goodview.GoodView;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.tools.CheckCache;
import com.ziyawang.ziyadaily.tools.DownLoadManager;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.NetUtils;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.CustomDialog;

import java.io.File;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class SysSettingActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private TextView common_title ;
    private RelativeLayout set_clean_relative ;
    private TextView set_clean_text ;
    private TextView set_ziya_rule ;
    private TextView set_ziya_share ;
    private String totalCacheSize ;
    private TextView logout ;
    private TextView app_check ;
    private TextView app_info ;

    private SharedPreferences loginCode ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_sys_setting);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        common_title = (TextView) findViewById(R.id.common_title ) ;
        set_clean_relative = (RelativeLayout)findViewById(R.id.set_clean_relative ) ;
        set_clean_text = (TextView)findViewById(R.id.set_clean_text ) ;
        set_ziya_rule = (TextView)findViewById(R.id.set_ziya_rule ) ;
        set_ziya_share = (TextView)findViewById(R.id.set_ziya_share) ;
        logout = (TextView)findViewById(R.id.logout) ;

        app_info = (TextView)findViewById(R.id.app_info ) ;
        app_check = (TextView)findViewById(R.id.app_check ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        set_ziya_rule.setOnClickListener(this);
        set_ziya_share.setOnClickListener(this);
        set_clean_relative.setOnClickListener(this);
        app_info.setOnClickListener(this);
        app_check.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GetBenSharedPreferences.getIsLogin(SysSettingActivity.this)){
            logout.setVisibility(View.VISIBLE);
        }else {
            logout.setVisibility(View.GONE);
        }
        common_title.setText(R.string.me_SysSetting);
    }

    @Override
    public void initData() {
        try {
            totalCacheSize = CheckCache.getTotalCacheSize(SysSettingActivity.this);
            set_clean_text.setText("" + totalCacheSize);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.set_ziya_rule :
                goMyRuleActivity() ;
                break;
            case R.id.set_ziya_share :
                showShare() ;
                break;
            case R.id.set_clean_relative :
                cleanCache() ;
                break;
            case R.id.app_info :
                showAppInfo() ;
                break;
            case R.id.app_check :
                checkVersion() ;
                break;
            case R.id.logout :
                logout() ;
                break;
            default:
                break;
        }
    }

    private void logout() {
        final CustomDialog.Builder builder = new CustomDialog.Builder(SysSettingActivity.this) ;
        builder.setTitle("温馨提示") ;
        builder.setMessage("退出后不会删除任何历史数据，下次登录依然可以使用本账号。");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                benLogout();
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

    private void benLogout() {
        loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        loginCode.edit().putString("loginCode", "").commit();

        SharedPreferences isLogin = getSharedPreferences("isLogin" , MODE_PRIVATE);
        isLogin.edit().putBoolean("isLogin", false).commit();



        Intent intent = new Intent(SysSettingActivity.this , LoginActivity.class ) ;
        startActivity(intent );

        ToastUtils.shortToast(SysSettingActivity.this , "退出登录成功");
    }

    private void showShare() {

        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle("资芽早报");
        //应用宝的官网的资芽的位置
        //oks.setTitleUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.ziyawang.ziya");
        oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.ziyawang.ziyadaily");
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png") ;
        oks.setText("资芽早报正式上线啦，小伙伴们赶快加入吧。");
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.ziyawang.ziya" );
        oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.ziyawang.ziyadaily" );
        // 启动分享GUI
        oks.show(this);

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
                            final CustomDialog.Builder builder = new CustomDialog.Builder(SysSettingActivity.this);
                            builder.setTitle(UpdateTitle);
                            builder.setMessage(UpdateDes);
                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    int networkType = NetUtils.getNetworkType(SysSettingActivity.this);
                                    switch (networkType){
                                        case NetUtils.NETTYPE_CMNET :
                                        case NetUtils.NETTYPE_CMWAP:
                                            CustomDialog.Builder customDialog = new CustomDialog.Builder(SysSettingActivity.this) ;
                                            customDialog.setTitle("亲爱的用户");
                                            customDialog.setMessage("当前未连接wifi，是否继续下载");
                                            customDialog.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    //下载apk
                                                    final ProgressDialog pd;    //进度条对话框
                                                    pd = new ProgressDialog(SysSettingActivity.this);
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
                                            pd = new  ProgressDialog(SysSettingActivity.this);
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
                        }else {
                            ToastUtils.shortToast(SysSettingActivity.this , "已经是最新版本");
                        }

                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(SysSettingActivity.this, "网络连接异常");
            }
        }) ;
    }

    private void showAppInfo() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;

        CustomDialog.Builder builder = new CustomDialog.Builder(SysSettingActivity.this);
        builder.setTitle("www.ziyawang.com");
        builder.setMessage("当前版本:  " + version + "  ，我们将尽我们最大的努力，提供给您最优质的体验。");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void cleanCache() {
        if (totalCacheSize.equals("0K")){
            ToastUtils.shortToast(SysSettingActivity.this , "已经清理至最佳状态");
        }else {
            CheckCache.clearAllCache(SysSettingActivity.this);
            ToastUtils.shortToast(SysSettingActivity.this , "缓存已清空");
            try {
                totalCacheSize = CheckCache.getTotalCacheSize(SysSettingActivity.this);
                set_clean_text.setText("" + totalCacheSize);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void goMyRuleActivity() {
        Intent intent = new Intent(SysSettingActivity.this , MyRuleActivity.class ) ;
        intent.putExtra("type" , "rule" ) ;
        startActivity(intent);
    }
}
