package com.ziyawang.ziyadaily.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyProgressDialog;

public class CustomizationDailyActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private TextView common_title ;
    private TextView pro_asset ;
    private TextView pro_rule ;
    private TextView text_land ;
    private TextView text_building ;
    private TextView text_car ;

    private Button submit ;
    private String types ;

    private LinearLayout one , two ;

    private MyProgressDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData() ;
    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        String urls = String.format(Url.returnType, GetBenSharedPreferences.getTicket(CustomizationDailyActivity.this ) ) ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("returnType" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.size(); i++) {
                            String s = data.get(i).toString();
                            if ("1".equals(s)){
                                showSelected(pro_asset) ;
                            }
                            if ("2".equals(s)){
                                showSelected(pro_rule) ;
                            }
                            if ("3".equals(s)){
                                showSelected(text_land) ;
                            }
                            if ("4".equals(s)){
                                showSelected(text_building) ;
                            }
                            if ("5".equals(s)){
                                showSelected(text_car) ;
                            }
                        }
                        one.setVisibility(View.VISIBLE);
                        two.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(CustomizationDailyActivity.this, "网络连接异常");
            }
        }) ;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_customization_daily);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        common_title = (TextView)findViewById(R.id.common_title ) ;
        pro_asset = (TextView)findViewById(R.id.pro_asset ) ;
        text_car = (TextView)findViewById(R.id.text_car ) ;
        text_building = (TextView)findViewById(R.id.text_building ) ;
        text_land = (TextView)findViewById(R.id.text_land ) ;
        pro_rule = (TextView)findViewById(R.id.pro_rule ) ;
        submit = (Button) findViewById(R.id.submit ) ;

        one = (LinearLayout)findViewById(R.id.one ) ;
        two = (LinearLayout)findViewById(R.id.two ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        pro_asset.setOnClickListener(this);
        pro_rule.setOnClickListener(this);
        text_land.setOnClickListener(this);
        text_building.setOnClickListener(this);
        text_car.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        common_title.setText(R.string.customizationDaily);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.pro_asset :
                changSelected(pro_asset ) ;
                break;
            case R.id.pro_rule :
                changSelected(pro_rule ) ;
                break;
            case R.id.text_land :
                changSelected(text_land ) ;
                break;
            case R.id.text_building :
                changSelected(text_building ) ;
                break;
            case R.id.text_car :
                changSelected(text_car ) ;
                break;
            case R.id.submit :
                toSubmit();
                break;
            default:
                break;
        }
    }

    private void toSubmit() {

        dialog = new MyProgressDialog(CustomizationDailyActivity.this , "正在提交，请稍后...") ;
        dialog.show();

        types = "" ;

        if (pro_asset.isSelected()){
            types += "1," ;
        }
        if (pro_rule.isSelected()){
            types += "2," ;
        }
        if (text_land.isSelected()){
            types += "3," ;
        }
        if (text_building.isSelected()){
            types += "4," ;
        }
        if (text_car.isSelected()){
            types += "5," ;
        }

        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("types" , types );
        String urls = String.format(Url.getLabel, GetBenSharedPreferences.getTicket(CustomizationDailyActivity.this ) ) ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("getLabel" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        ToastUtils.shortToast(CustomizationDailyActivity.this , "定制成功");
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
                ToastUtils.shortToast(CustomizationDailyActivity.this, "网络连接异常");
            }
        }) ;
    }



    private void changSelected(TextView textView) {
        if (textView.isSelected()){
            textView.setSelected(false);
            textView.setTextColor(Color.rgb(51,51,51));
            Drawable drawable = getResources().getDrawable(R.mipmap.add_pro);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        }else {
            showSelected(textView) ;
        }
    }

    private void showSelected(TextView textView) {
        textView.setSelected(true);
        textView.setTextColor(Color.rgb(253,207,0));
        Drawable drawable = getResources().getDrawable(R.mipmap.xuanzhong);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }
}
