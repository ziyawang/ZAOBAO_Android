package com.ziyawang.ziyadaily.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.adapter.GoldDetailsAdapter;
import com.ziyawang.ziyadaily.entity.GoldDetailsEntity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyProgressDialog;

import java.util.List;

public class GoldRecordActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre;
    private TextView gold_type;

    private MyProgressDialog dialog;
    //搜索type
    private String type;
    private List<GoldDetailsEntity> list;
    private ListView listView;
    private GoldDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载数据
        loadData(type);
    }

    private void loadData(String type) {
        dialog = new MyProgressDialog(GoldRecordActivity.this, "加载数据中请稍后。。。");
        dialog.show();
        String urls = String.format(Url.myBill, GetBenSharedPreferences.getTicket(GoldRecordActivity.this));
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("Type", type);
        params.addQueryStringParameter("startpage", "1");
        params.addQueryStringParameter("pagecount", "1000000");
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("myBill" , responseInfo.result ) ;

                if (dialog != null) {
                    dialog.dismiss();
                }
                //处理数据
                dealResult(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                if (dialog != null) {
                    dialog.dismiss();
                }
                error.printStackTrace();
                ToastUtils.shortToast(GoldRecordActivity.this, "网络连接异常");
            }
        });
    }

    private void dealResult(String result) {
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(result);
        JSONArray data = object.getJSONArray("data");
        list = JSON.parseArray(data.toJSONString(), GoldDetailsEntity.class);
        adapter = new GoldDetailsAdapter(GoldRecordActivity.this, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GoldRecordActivity.this, GoldRecord02Activity.class);
                //1是充值，2是消费
                intent.putExtra("money", list.get(position).getMoney());
                intent.putExtra("type", list.get(position).getType());
                intent.putExtra("time", list.get(position).getCreated_at());
                intent.putExtra("orderNumber", list.get(position).getOrderNumber());
                intent.putExtra("operates", list.get(position).getOperates());
                startActivity(intent);
            }
        });
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_gold_record);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout) findViewById(R.id.pre);
        gold_type = (TextView) findViewById(R.id.gold_type);
        listView = (ListView)findViewById(R.id.listView ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        gold_type.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre:
                finish();
                break;
            case R.id.gold_type :
                showPopUpWindow() ;
                break;
            default:
                break;
        }
    }

    private void showPopUpWindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_gold_type, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RelativeLayout my_gold_relative = (RelativeLayout)findViewById(R.id.my_gold_relative) ;
        final CheckBox a = (CheckBox)view.findViewById(R.id.one_one);
        final CheckBox b = (CheckBox)view.findViewById(R.id.one_two);
        final CheckBox c = (CheckBox)view.findViewById(R.id.one_three);

        Log.e("GoldDetailsActivitys" , gold_type.getText().toString() ) ;
        switch (gold_type.getText().toString()){
            case "全部" :
                a.setChecked(true);
                break;
            case "充值" :
                b.setChecked(true);
                break;
            case "付费" :
                c.setChecked(true);
                break;
            default:
                break;
        }
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                list.clear();
                gold_type.setText("全部");
                type = "" ;
                loadData(type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                list.clear();
                gold_type.setText("充值");
                type = "1" ;
                loadData(type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                list.clear();
                gold_type.setText("付费");
                type = "2" ;
                loadData(type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(my_gold_relative);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
