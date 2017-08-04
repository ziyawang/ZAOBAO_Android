package com.ziyawang.ziyadaily.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.ziyawang.ziyadaily.activity.CustomizationDailyActivity;
import com.ziyawang.ziyadaily.activity.LoginActivity;
import com.ziyawang.ziyadaily.adapter.HomeAdapter;
import com.ziyawang.ziyadaily.entity.HomeEntity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2017/6/12.
 */

public class HomeItemFragment extends Fragment implements View.OnClickListener{


    private ListView listView ;
    private HomeAdapter adapter ;
    private MyProgressDialog dialog ;
    private SwipeRefreshLayout swipeRefreshLayout ;

    private List<HomeEntity> ben_data = new ArrayList<HomeEntity>() ;
    private int startpage = 1 ;

    private String type ;

    private LinearLayout footLinearLayout ;
    private TextView ben ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        type = (String)getArguments().get("type");
        if (ben_data.size() != 0 ){
            sbCode() ;
        }
    }

    private void sbCode() {
        /***********************************************SB代码******************************************/
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("startpage" , "1");
        params.addQueryStringParameter("pagecount" , ben_data.size() + "" );
        params.addQueryStringParameter("label" , type );
        String urls ;
        if (null == GetBenSharedPreferences.getTicket(getActivity()) ){
            urls = String.format(Url.HomeData103 , "" ) ;
        }else {
            urls = String.format(Url.HomeData103,GetBenSharedPreferences.getTicket(getActivity()) ) ;
        }
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        httpUtils.send(HttpRequest.HttpMethod.GET,urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null ){
                    dialog.dismiss();
                }
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.e("HomeData103" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        JSONArray data = object.getJSONArray("data");
                        if (data.size() != 0 ){
                            List<HomeEntity> list = JSON.parseArray(data.toJSONString(), HomeEntity.class);
                            ben_data.clear();
                            ben_data.addAll(list) ;
                            adapter.notifyDataSetChanged();

                            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                                    switch (scrollState) {
                                        case SCROLL_STATE_IDLE:
                                            if (isListViewReachBottomEdge(absListView)){
                                                dialog = new MyProgressDialog(getActivity() , "加载数据中请稍后。。。") ;
                                                dialog.show();
                                                Log.e("测试" , "测试" ) ;
                                                loadData();
                                            }

                                            break;
                                    }
                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                }
                            });
                        }else {
                            ben.setText("没有更多数据");
                        }
                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(getActivity(), "网络连接异常");
            }
        }) ;
        /***********************************************SB代码******************************************/
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
                sbCode();
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化组件
        initView(view) ;
        //注册监听事件
        initListeners() ;
        //获取数据
        initData() ;
        //加载数据
        loadData();
    }

    private void initData() {
        type = (String)getArguments().get("type");
    }

    private void initListeners() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startpage = 1 ;
                ben_data.clear();
                Log.e("swipe" , "swipe") ;
                loadData();
            }
        });
    }

    private void initView(View view) {

        footLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_foot_item , null ) ;
        ben = (TextView)footLinearLayout.findViewById(R.id.ben ) ;
        listView = (ListView) view.findViewById(R.id.listView ) ;
        listView.setDivider(null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout ) ;
        swipeRefreshLayout.setColorSchemeResources(R.color.common_bg);
        adapter = new HomeAdapter(getActivity() , ben_data ) ;
        listView.addFooterView(footLinearLayout);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
        }
    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("startpage" , startpage + "");
        params.addQueryStringParameter("pagecount" , "10");
        params.addQueryStringParameter("label" , type );
        String urls ;
        if (null == GetBenSharedPreferences.getTicket(getActivity()) ){
            urls = String.format(Url.HomeData103 , "" ) ;
        }else {
            urls = String.format(Url.HomeData103,GetBenSharedPreferences.getTicket(getActivity()) ) ;
        }
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        httpUtils.send(HttpRequest.HttpMethod.GET,urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null ){
                    dialog.dismiss();
                }
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.e("HomeData" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        startpage += 1 ;
                        JSONArray data = object.getJSONArray("data");
                        if (data.size() != 0 ){
                            List<HomeEntity> list = JSON.parseArray(data.toJSONString(), HomeEntity.class);
                            ben_data.addAll(list) ;
                            adapter.notifyDataSetChanged();

                            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                                    switch (scrollState) {
                                        case SCROLL_STATE_IDLE:
                                            if (isListViewReachBottomEdge(absListView)){
                                                dialog = new MyProgressDialog(getActivity() , "加载数据中请稍后。。。") ;
                                                dialog.show();
                                                Log.e("测试" , "测试" ) ;
                                                loadData();
                                            }

                                            break;
                                    }
                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                }
                            });
                        }else {
                            ben.setText("没有更多数据");
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
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                error.printStackTrace();
                ToastUtils.shortToast(getActivity(), "网络连接异常");
            }
        }) ;
    }

    public boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result;
    }

}
