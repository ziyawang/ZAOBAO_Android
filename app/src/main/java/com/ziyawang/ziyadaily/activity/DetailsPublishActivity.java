package com.ziyawang.ziyadaily.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyProgressDialog;

public class DetailsPublishActivity extends BenBenActivity implements View.OnClickListener{

    private String type ;
    private RelativeLayout pre ;
    private TextView common_title ;

    private EditText des , companyName , person , phone ;
    private Button submit ;

    private MyProgressDialog dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_details_publish);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        common_title = (TextView)findViewById(R.id.common_title ) ;

        des = (EditText) findViewById(R.id.des ) ;
        companyName = (EditText) findViewById(R.id.companyName ) ;
        person = (EditText) findViewById(R.id.person ) ;
        phone = (EditText) findViewById(R.id.phone ) ;

        submit = (Button) findViewById(R.id.submit ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        type = intent.getStringExtra("type");
        if ("1".equals(type)){
            common_title.setText(R.string.SaleAsset);
        }else {
            common_title.setText(R.string.BuyRequired);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.submit :
                if (isCheck()){
                    toSubmit();
                }
                break;
            default:
                break;
        }
    }

    private boolean isCheck() {
        if (TextUtils.isEmpty(des.getText().toString().trim())){
            ToastUtils.shortToast(DetailsPublishActivity.this , "请输入内容");
            return false ;
        }
        if (TextUtils.isEmpty(companyName.getText().toString().trim())){
            ToastUtils.shortToast(DetailsPublishActivity.this , "请输入公司名");
            return false ;
        }
        if (TextUtils.isEmpty(person.getText().toString().trim())){
            ToastUtils.shortToast(DetailsPublishActivity.this , "请输入联系人");
            return false ;
        }
        if (TextUtils.isEmpty(phone.getText().toString().trim())){
            ToastUtils.shortToast(DetailsPublishActivity.this , "请输入电话");
            return false ;
        }
        if (!phone.getText().toString().trim().matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
            ToastUtils.longToast(DetailsPublishActivity.this , "请输入正确的手机号码");
            return false ;
        }
        return true;
    }

    private void toSubmit() {
        dialog = new MyProgressDialog(DetailsPublishActivity.this , "发布中，请稍后...") ;
        dialog.show();
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("describe" , des.getText().toString().trim());
        params.addBodyParameter("company" , companyName.getText().toString().trim());
        params.addBodyParameter("connecter" , person.getText().toString().trim());
        params.addBodyParameter("phone" , phone.getText().toString().trim());
        params.addBodyParameter("type" , type);
        String urls = String.format(Url.publish, GetBenSharedPreferences.getTicket(DetailsPublishActivity.this ) ) ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("publish" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        ToastUtils.shortToast(DetailsPublishActivity.this , "发布成功");
                        finish();
                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                error.printStackTrace();
                ToastUtils.shortToast(DetailsPublishActivity.this, "网络连接异常");
            }
        }) ;
    }
}
