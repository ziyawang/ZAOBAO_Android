package com.ziyawang.ziyadaily.activity;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.adapter.MyPublishAdapter;
import com.ziyawang.ziyadaily.entity.MyPublishEntity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyProgressDialog;

import java.util.List;

import static com.ziyawang.ziyadaily.R.id.listView;

public class FeedBackActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private TextView common_title ;

    private EditText des , phone , person  ;
    private Button submit ;

    private MyProgressDialog dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_feed_back);
    }

    @Override
    public void initViews() {
        common_title = (TextView)findViewById(R.id.common_title ) ;
        des = (EditText) findViewById(R.id.des ) ;
        phone = (EditText)findViewById(R.id.phone ) ;
        person = (EditText)findViewById(R.id.person ) ;
        submit = (Button) findViewById(R.id.submit ) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        common_title.setText(R.string.me_FeedBack);
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
            ToastUtils.shortToast(FeedBackActivity.this , "请输入内容");
            return false ;
        }
        if (TextUtils.isEmpty(person.getText().toString().trim())){
            ToastUtils.shortToast(FeedBackActivity.this , "请输入联系人");
            return false ;
        }
        if (TextUtils.isEmpty(phone.getText().toString().trim())){
            ToastUtils.shortToast(FeedBackActivity.this , "请输入电话");
            return false ;
        }
        if (!phone.getText().toString().trim().matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
            ToastUtils.longToast(FeedBackActivity.this , "请输入正确的手机号码");
            return false ;
        }
        return true;
    }

    private void toSubmit() {
        dialog = new MyProgressDialog(FeedBackActivity.this , "意见提交中，请稍后...") ;
        dialog.show();
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("content" , des.getText().toString().trim());
        params.addBodyParameter("connecter" , person.getText().toString().trim());
        params.addBodyParameter("phone" , phone.getText().toString().trim());
        String urls ;
        if (null == GetBenSharedPreferences.getTicket(FeedBackActivity.this) ){
            urls = String.format(Url.note , "" ) ;
        }else {
            urls = String.format(Url.note , GetBenSharedPreferences.getTicket(FeedBackActivity.this) ) ;
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("note" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        ToastUtils.shortToast(FeedBackActivity.this , "反馈成功");
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
                ToastUtils.shortToast(FeedBackActivity.this, "网络连接异常");
            }
        }) ;
    }
}
