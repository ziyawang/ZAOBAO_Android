package com.ziyawang.ziyadaily.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyProgressDialog;
import com.ziyawang.ziyadaily.views.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private RelativeLayout toRegister ;
    private RelativeLayout losePwd ;
    private XEditText edit_pwd ;
    private Button login ;
    private XEditText edit_phoneNumber ;
    private MyProgressDialog dialog ;

    //sp
    private SharedPreferences loginCode ;
    private SharedPreferences isLogin ;
    private SharedPreferences userId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        toRegister = (RelativeLayout)findViewById(R.id.toRegister ) ;
        losePwd = (RelativeLayout)findViewById(R.id.losePwd ) ;
        edit_phoneNumber = (XEditText) findViewById(R.id.edit_phoneNumber ) ;
        edit_pwd = (XEditText) findViewById(R.id.edit_pwd ) ;
        login = (Button) findViewById(R.id.login ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        toRegister.setOnClickListener(this);
        losePwd.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void initData() {
        edit_phoneNumber.setSeparator(" ");
        edit_phoneNumber.setPattern(new int[]{3, 4, 4});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.losePwd :
                Intent intent01 = new Intent(LoginActivity.this , RegisterActivity.class ) ;
                intent01.putExtra("type" , "2" ) ;
                startActivity(intent01);
                break;
            case R.id.toRegister :
                Intent intent = new Intent(LoginActivity.this , RegisterActivity.class ) ;
                intent.putExtra("type" , "1" ) ;
                startActivity(intent);
                break;
            case R.id.login:
                submit() ;
                break;
            default:
                break;
        }
    }

    private void submit() {
        final String replace = edit_phoneNumber.getText().toString().replace(" ", "");
        if (judgePwd(replace)){
            //显示Dialog
            shoeDialog() ;
            //进行网络请求
            loadData() ;
        }
    }

    private void loadData() {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        //将用户名和密码封装到Post体里面
        params.addQueryStringParameter("phonenumber" , edit_phoneNumber.getText().toString().replace(" ","").trim() );
        params.addQueryStringParameter("password" , edit_pwd.getText().toString().trim() );
        //发送请求
        utils.send(HttpRequest.HttpMethod.GET, Url.login, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("Login", responseInfo.result) ;
                //隐藏dialog
                hiddenDialog() ;
                //处理result
                dealResult(responseInfo.result) ;
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //隐藏dialog
                hiddenDialog() ;
                //打印失败毁掉的log
                error.printStackTrace();
                //提示用户
                ToastUtils.shortToast(LoginActivity.this , "网络连接异常");
            }
        });
    }

    private void dealResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            final String status_code = jsonObject.getString("status_code");
            switch(status_code){
                case "200" :
                    //将sp存储
                    initSp(jsonObject) ;

                    //登陆成功，跳转到主页面,并关闭此页面
                    finish();
                    ToastUtils.shortToast(LoginActivity.this  , "登录成功");
                    break;
                case "404":
                    ToastUtils.shortToast(LoginActivity.this, "用户名或密码错误");
                    break;
                case "405":
                    ToastUtils.shortToast(LoginActivity.this, "账号被冻结");
                    break;
                case "406":
                    ToastUtils.shortToast(LoginActivity.this , "账号不存在");
                    break;
                case "502":
                    ToastUtils.shortToast(LoginActivity.this , "服务器异常，请稍后重试。");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initSp(JSONObject jsonObject) throws JSONException {
        final String ticket = jsonObject.getString("token");
        String userID = jsonObject.getString("UserID");

        loginCode = getSharedPreferences("loginCode" , MODE_PRIVATE );
        isLogin = getSharedPreferences("isLogin" , MODE_PRIVATE ) ;
        userId = getSharedPreferences("userId" , MODE_PRIVATE ) ;

        loginCode.edit().putString("loginCode", ticket).commit();
        isLogin.edit().putBoolean("isLogin", true).commit();
        userId.edit().putString("userId", userID).commit();
    }

    private void hiddenDialog() {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    private void shoeDialog() {
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(LoginActivity.this , "正在登录，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
    }

    private boolean judgePwd(String edit_phoneNumber) {
        if (TextUtils.isEmpty(edit_phoneNumber)){
            ToastUtils.longToast(LoginActivity.this, "请输入手机号");
            return false ;
        }
        if (!edit_phoneNumber.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
            ToastUtils.longToast(LoginActivity.this , "请输入正确的手机号码");
            return false ;
        }
        if (TextUtils.isEmpty(edit_pwd.getText().toString())){
            ToastUtils.longToast(LoginActivity.this , "请输入密码");
            return false ;
        }
        if (edit_pwd.getText().toString().length() < 6){
            ToastUtils.longToast(LoginActivity.this, "请至少输入6位密码");
            return false ;
        }
        if (edit_pwd.getText().toString().length() > 16){
            ToastUtils.longToast(LoginActivity.this, "至多输入16位密码");
            return false ;
        }
        return true ;
    }
}
