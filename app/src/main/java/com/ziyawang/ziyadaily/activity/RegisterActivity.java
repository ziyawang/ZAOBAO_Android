package com.ziyawang.ziyadaily.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.application.MyApplication;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyProgressDialog;
import com.ziyawang.ziyadaily.views.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private Button btn_to ;
    private TextView text_title ;
    private String type ;
    private XEditText edit_phoneNumber ;
    private XEditText edit_smsCode ;
    private XEditText edit_pwd ;
    private XEditText edit_pwd_again ;
    private TextView get_smsCode ;
    private MyProgressDialog dialog , myProgressDialog ;
    private SharedPreferences loginCode ;
    private SharedPreferences isLogin ;
    private SharedPreferences userId ;
    private MyApplication app ;
    private String action ;
    //发送验证阿60s后，才可以再次发送。
    private int recLen =60;
    //private Timer timer = new Timer() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        btn_to = (Button) findViewById(R.id.btn_to ) ;
        text_title = (TextView) findViewById(R.id.text_title ) ;

        edit_phoneNumber = (XEditText)findViewById(R.id.edit_phoneNumber) ;
        edit_smsCode = (XEditText)findViewById(R.id.edit_smsCode) ;
        edit_pwd = (XEditText)findViewById(R.id.edit_pwd) ;
        edit_pwd_again = (XEditText)findViewById(R.id.edit_pwd_again) ;
        get_smsCode = (TextView) findViewById(R.id.get_smsCode) ;

        edit_phoneNumber.setSeparator(" ");
        edit_phoneNumber.setPattern(new int[]{3, 4, 4});
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this );
        btn_to.setOnClickListener(this );
        get_smsCode.setOnClickListener(this );
    }

    @Override
    public void initData() {
        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);

        Intent intent = getIntent() ;
        type = intent.getStringExtra("type");
        if ("1".equals(type)){
            text_title.setText(R.string.registerId);
            btn_to.setText(R.string.register);
            action = "register" ;
        }else if ("2".equals(type)){
            text_title.setText(R.string.findBackPwd);
            btn_to.setText("登录");
            action = "login" ;
        }else {
            text_title.setText(null);
            btn_to.setText(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.btn_to :
                toRegister();
                break;
            case R.id.get_smsCode :
                //获取验证码
                registerSmsCode() ;
                break;
            default:
                break;
        }
    }

    private void toRegister() {
        final String phoneNumber = edit_phoneNumber.getText().toString().replace(" ","");

        if (!TextUtils.isEmpty(phoneNumber)){
            final String muserName = phoneNumber ;
            //判断输入的账号是否是一个真实有效的手机号
            if (muserName.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){

                //判断验证码是否为空
                if (!TextUtils.isEmpty(edit_smsCode.getText().toString().replace(" ", ""))){
                    String smsCode = edit_smsCode.getText().toString().replace(" ", "") ;

                    //判断密码是否为空
                    if (!TextUtils.isEmpty(edit_pwd.getText().toString())){
                        String mpwd = edit_pwd.getText().toString();
                        if (mpwd.length() < 6 ){
                            ToastUtils.longToast(RegisterActivity.this, "请至少输入6位密码");
                        }else if (mpwd.length() > 16){
                            ToastUtils.longToast(RegisterActivity.this , "至多输入16位密码");
                        }else {
                            if (!TextUtils.isEmpty(edit_pwd_again.getText().toString())){
                                //判断两次驶入的密码时候一致
                                if (edit_pwd_again.getText().toString().equals(edit_pwd.getText().toString())){

                                /* 显示ProgressDialog */
                                    //在开始进行网络连接时显示进度条对话框
                                    dialog = new MyProgressDialog(RegisterActivity.this , "加载中请稍后...");
                                    dialog.setCancelable(false);// 不可以用“返回键”取消
                                    dialog.show();

                                    //进行网络请求
                                    HttpUtils utils = new HttpUtils();
                                    RequestParams params = new RequestParams();
                                    //将用户名和密码封装到Post体里面
                                    params.addBodyParameter("phonenumber" , muserName);
                                    params.addBodyParameter("password" , mpwd );
                                    params.addBodyParameter("smscode" , smsCode );


                                    final String urls ;
                                    if ("register".equals(action)){
                                        urls = Url.register ;
                                        params.addBodyParameter("channel" , "ANDROID");
                                    }else {
                                        urls = Url.resetPassword ;
                                    }

                                    //发送请求
                                    utils.send( HttpRequest.HttpMethod.POST , urls , params, new RequestCallBack<String>() {
                                        //失败回调
                                        @Override
                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                            Log.e("register", responseInfo.result) ;
                                            if (dialog!=null){
                                                dialog.dismiss();
                                            }
                                            try {
                                                JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                final String status_code = jsonObject.getString("status_code");
                                                switch(status_code){
                                                    case "200" :
                                                        final String ticket = jsonObject.getString("token");
                                                        String userID = jsonObject.getString("UserID");
                                                        loginCode = getSharedPreferences("loginCode" , MODE_PRIVATE );
                                                        isLogin = getSharedPreferences("isLogin" , MODE_PRIVATE ) ;
                                                        userId = getSharedPreferences("userId" , MODE_PRIVATE ) ;
                                                        loginCode.edit().putString("loginCode", ticket).commit();
                                                        isLogin.edit().putBoolean("isLogin", true).commit();
                                                        userId.edit().putString("userId", userID).commit();

                                                        finish();
                                                        MyApplication.finishSingleActivityByClass(LoginActivity.class ) ;
                                                        if ("register".equals(action)){
                                                            ToastUtils.shortToast(RegisterActivity.this, "注册成功");
                                                        }else {
                                                            ToastUtils.shortToast(RegisterActivity.this, "找回密码成功");
                                                        }


                                                        break;
                                                    case "405":
                                                        ToastUtils.shortToast(RegisterActivity.this, "该账号已注册，请直接登录");
                                                        break;
                                                    case "402":
                                                        ToastUtils.shortToast(RegisterActivity.this , "验证码错误");
                                                        break;
                                                    case "401":
                                                        ToastUtils.shortToast(RegisterActivity.this , "参数错误");
                                                        break;
                                                    case "501":
                                                        ToastUtils.shortToast(RegisterActivity.this , "服务器异常，请稍后重试");
                                                        break;
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //成功回掉
                                        @Override
                                        public void onFailure(HttpException error, String msg) {
                                            if (dialog!=null){
                                                dialog.dismiss();
                                            }
                                            error.printStackTrace();
                                            ToastUtils.shortToast(RegisterActivity.this , "网络连接异常");
                                        }
                                    });
                                }else{
                                    ToastUtils.longToast(RegisterActivity.this , "两次密码输入不一致");
                                }
                            }else {
                                ToastUtils.longToast(RegisterActivity.this, "请再次输入密码");
                            }


                        }

                    }else {
                        ToastUtils.longToast(RegisterActivity.this, "请输入密码");
                    }

                }else{
                    ToastUtils.longToast(RegisterActivity.this, "请输入验证码");
                }

            }else {
                ToastUtils.longToast(RegisterActivity.this , "请输入正确的手机号码");
            }
        }else {
            ToastUtils.longToast(RegisterActivity.this, "请输入手机号");
        }
    }


    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    get_smsCode.setText(recLen + " s");

                    if(recLen > 0){
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    }else{
                        get_smsCode.setOnClickListener(RegisterActivity.this);
                        get_smsCode.setText(R.string.getCode);
                    }
            }

            super.handleMessage(msg);
        }
    };

    //获取验证码
    private void registerSmsCode() {

        final String phoneNumber = edit_phoneNumber.getText().toString().replace(" ","");
        if (!TextUtils.isEmpty(phoneNumber)){
            String muserName = phoneNumber ;
            //判断输入的账号是否是一个真实有效的手机号
            if (muserName.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){

                /* 显示ProgressDialog */
                //在开始进行网络连接时显示进度条对话框
                myProgressDialog = new MyProgressDialog(RegisterActivity.this , "获取短信验证码，请稍后...");
                myProgressDialog.setCancelable(false);// 不可以用“返回键”取消
                myProgressDialog.show();

                //请求smsCode
                HttpUtils httpUtils = new HttpUtils() ;
                httpUtils.configCurrentHttpCacheExpiry(1000) ;
                RequestParams params = new RequestParams() ;
                params.addQueryStringParameter("phonenumber", muserName);
                params.addQueryStringParameter("action", action );

                httpUtils.send(HttpRequest.HttpMethod.GET, Url.getSmsCode, params , new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        myProgressDialog.dismiss();

                        Log.e("getSmsCode", responseInfo.result);
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            final String status_code = jsonObject.getString("status_code");
                            switch (status_code) {
                                case "200":
                                    ToastUtils.longToast(RegisterActivity.this, "验证码发送成功");
                                    Message message = handler.obtainMessage(1);     // Message
                                    handler.sendMessageDelayed(message, 1000);
                                    get_smsCode.setOnClickListener(null);
                                    break;
                                case "401":
                                    ToastUtils.longToast(RegisterActivity.this, "参数不正确");
                                    break;
                                case "403":
                                    ToastUtils.longToast(RegisterActivity.this, "验证码发送失败");
                                    break;
                                case "405":
                                    ToastUtils.longToast(RegisterActivity.this, "该账户已注册，请直接登录");
                                    break;
                                case "406":
                                    ToastUtils.longToast(RegisterActivity.this, "该账户未注册，请注册");
                                    break;
                                case "503":
                                    ToastUtils.longToast(RegisterActivity.this, "服务器错误，验证码发送失败");
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        myProgressDialog.dismiss();
                        error.printStackTrace();
                        ToastUtils.longToast(RegisterActivity.this, "网络连接异常");
                    }
                }) ;

            }else {
                ToastUtils.longToast(RegisterActivity.this , "请输入正确的手机号码");
            }
        }else {
            ToastUtils.longToast(RegisterActivity.this, "请输入手机号");
        }
    }
}
