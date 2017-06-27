package com.ziyawang.ziyadaily.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.ziyawang.ziyadaily.adapter.MyPublishListAdapter;
import com.ziyawang.ziyadaily.entity.MyPublishEntity;
import com.ziyawang.ziyadaily.entity.MyPublishListEntity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;

import java.util.List;

public class MyPublishListActivity extends BenBenActivity implements View.OnClickListener {

    private ListView listView ;
    private TextView info_data_view ;
    private RelativeLayout pre ;
    private TextView common_title ;
    private String type ;
    private MyPublishListAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData() ;
    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("pagecount" , "10000");
        params.addBodyParameter("type" , type );
        String urls = String.format(Url.publishList, GetBenSharedPreferences.getTicket(MyPublishListActivity.this ) ) ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("publishList" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        JSONArray data = object.getJSONArray("data");
                        final List<MyPublishListEntity> list = JSON.parseArray(data.toJSONString(), MyPublishListEntity.class);
                        adapter = new MyPublishListAdapter(MyPublishListActivity.this , list ) ;
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
                ToastUtils.shortToast(MyPublishListActivity.this, "网络连接异常");
            }
        }) ;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my_publish_list);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        listView = (ListView)findViewById(R.id.listView) ;
        common_title = (TextView) findViewById(R.id.common_title ) ;

    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this) ;
    }

    @Override
    public void initData() {
        Intent intent =getIntent() ;
        type = intent.getStringExtra("type");
        if ("1".equals(type)){
            common_title.setText("出售资产");
        }else {
            common_title.setText("求购需求");
        }
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
