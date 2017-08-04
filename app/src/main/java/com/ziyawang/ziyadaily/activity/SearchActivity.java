package com.ziyawang.ziyadaily.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.adapter.HomeAdapter;
import com.ziyawang.ziyadaily.adapter.SearchAdapter;
import com.ziyawang.ziyadaily.adapter.SearchGridViewAdapter;
import com.ziyawang.ziyadaily.entity.HomeEntity;
import com.ziyawang.ziyadaily.entity.SearchGridViewEntity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyProgressDialog;
import com.ziyawang.ziyadaily.views.XEditText;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre;
    private XEditText edit_search;
    private GridView gridView ;
    private SearchGridViewAdapter adapter ;

    private LinearLayout linear_title ;
    private ListView listView ;
    private MyProgressDialog dialog ;
    private SearchAdapter searchAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData() ;

    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.SearchTitle , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("SearchTitle" , responseInfo.result ) ;
                    com.alibaba.fastjson.JSONObject object = JSON.parseObject(responseInfo.result);
                    String status_code = object.getString("status_code");
                    switch (status_code){
                        case "200" :
                            com.alibaba.fastjson.JSONArray data = object.getJSONArray("data");
                            final List<SearchGridViewEntity> list = JSON.parseArray(data.toJSONString(), SearchGridViewEntity.class);
                            adapter = new SearchGridViewAdapter(SearchActivity.this , list ) ;
                            gridView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    loadSearchData(list.get(position).getTitle());
                                    edit_search.setText(list.get(position).getTitle());
                                }
                            });
                            break;
                        default:
                            break;
                    }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast( SearchActivity.this, "网络连接异常");
            }
        }) ;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout) findViewById(R.id.pre);
        edit_search = (XEditText) findViewById(R.id.edit_search);
        gridView = (GridView) findViewById(R.id.gridView);
        linear_title = (LinearLayout) findViewById(R.id.linear_title);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
    }

    @Override
    public void initData() {
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(edit_search.getText().toString().trim())){
                        loadSearchData(edit_search.getText().toString().trim()) ;
                    }else {
                        ToastUtils.shortToast(SearchActivity.this , "请输入您要输入的内容");
                    }

                }
                return false;
            }

        });

        edit_search.setFocusable(true);
        edit_search.setFocusableInTouchMode(true);
        edit_search.requestFocus();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE );
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //InputMethodManager inputManager = (InputMethodManager) edit_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputManager.showSoftInput(edit_search, 0);
    }


    private void loadSearchData(String str ) {
        dialog = new MyProgressDialog(SearchActivity.this , "数据加载中，请稍后...") ;
        dialog.show();
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pagecount" , "10000");
        params.addQueryStringParameter("label" , "0");
        params.addQueryStringParameter("serName" , str );
        String urls ;
        if (null == GetBenSharedPreferences.getTicket(SearchActivity.this) ){
            urls = String.format(Url.HomeData103 , "" ) ;
        }else {
            urls = String.format(Url.HomeData103,GetBenSharedPreferences.getTicket(SearchActivity.this) ) ;
        }
        httpUtils.send(HttpRequest.HttpMethod.GET,urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null ){
                    dialog.dismiss();
                }
                Log.e("HomeData" , responseInfo.result ) ;
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        com.alibaba.fastjson.JSONArray data = object.getJSONArray("data");
                        if (data.size() != 0 ){
                            List<HomeEntity> list = JSON.parseArray(data.toJSONString(), HomeEntity.class);
                            searchAdapter = new SearchAdapter(SearchActivity.this , list ) ;
                            listView.setAdapter(searchAdapter);
                            adapter.notifyDataSetChanged();
                            linear_title.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }else {
                            ToastUtils.shortToast(SearchActivity.this, "未查询到任何数据");
                            linear_title.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null ){
                    dialog.dismiss();
                }
                error.printStackTrace();
                ToastUtils.shortToast(SearchActivity.this, "网络连接异常");
            }
        }) ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        finish();
                        overridePendingTransition(R.anim.in, R.anim.out);
                    }

                }, 100);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果按下的是返回键，并且没有重复
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.in, R.anim.out);
            return false;
        }
        return false;
    }
}
