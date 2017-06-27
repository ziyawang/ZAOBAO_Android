package com.ziyawang.ziyadaily.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
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

import java.util.List;

public class MyPublishActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private TextView common_title ;
    private ListView listView ;
    private TextView info_data_view ;
    private MyPublishAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData() ;
    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        String urls = String.format(Url.myPublish, GetBenSharedPreferences.getTicket(MyPublishActivity.this ) ) ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("mySystem" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        JSONArray data = object.getJSONArray("data");
                        final List<MyPublishEntity> list = JSON.parseArray(data.toJSONString(), MyPublishEntity.class);
                        adapter = new MyPublishAdapter(MyPublishActivity.this , list ) ;
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        break;
                    case "400" :
                        info_data_view.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(MyPublishActivity.this, "网络连接异常");
            }
        }) ;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my_publish);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        common_title = (TextView) findViewById(R.id.common_title ) ;
        listView = (ListView)findViewById(R.id.listView );
        info_data_view = (TextView) findViewById(R.id.info_data_view );
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this );
    }

    @Override
    public void initData() {
        common_title.setText(R.string.me_MyPublish);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            default:
                break;
        }
    }
}
