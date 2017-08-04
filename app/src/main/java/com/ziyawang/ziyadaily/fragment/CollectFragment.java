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
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.ziyawang.ziyadaily.activity.CustomizationDailyActivity;
import com.ziyawang.ziyadaily.activity.LoginActivity;
import com.ziyawang.ziyadaily.activity.SearchActivity;
import com.ziyawang.ziyadaily.adapter.CollectAdapter;
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

public class CollectFragment extends Fragment implements View.OnClickListener {

    private TextView customizationDaily;
    private ListView listView;
    private CollectAdapter adapter;
    private TextView info_data_view;
    private RelativeLayout relative_logo;
    private TextView btn_login;
    private MyProgressDialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<HomeEntity> ben_data = new ArrayList<HomeEntity>();
    private int startpage = 1;

    private LinearLayout headLinearLayout ;
    private TextView title_head ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化组件
        initView(view);
        //注册监听事件
        initListeners();
    }

    private void initListeners() {
        customizationDaily.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    startpage = 1;
                    ben_data.clear();
                    Log.e("swipe", "swipe");
                    loadData();
                }else {
                    ToastUtils.shortToast(getActivity() , "还未登录");
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

            }
        });
    }

    private void initView(View view) {
        customizationDaily = (TextView) view.findViewById(R.id.customizationDaily);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setDivider(null);
        info_data_view = (TextView) view.findViewById(R.id.info_data_view);
        relative_logo = (RelativeLayout) view.findViewById(R.id.relative_logo);
        btn_login = (TextView) view.findViewById(R.id.btn_login);

        headLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.collect_head , null ) ;
        title_head = (TextView)headLinearLayout.findViewById(R.id.title_head ) ;

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.common_bg);
        adapter = new CollectAdapter(getActivity(), ben_data , title_head , listView , info_data_view );
        listView.addHeaderView(headLinearLayout);
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customizationDaily:
                if (GetBenSharedPreferences.getIsLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in , R.anim.out );
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_login:
                goLoginActivity();
                break;
            default:
                break;
        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (GetBenSharedPreferences.getIsLogin(getActivity())) {
                listView.setVisibility(View.VISIBLE);
                info_data_view.setVisibility(View.GONE);
                relative_logo.setVisibility(View.GONE);
                startpage = 1;
                ben_data.clear();
                loadData();
            } else {
                listView.setVisibility(View.GONE);
                info_data_view.setVisibility(View.GONE);
                relative_logo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GetBenSharedPreferences.getIsLogin(getActivity())) {
            listView.setVisibility(View.VISIBLE);
            info_data_view.setVisibility(View.GONE);
            relative_logo.setVisibility(View.GONE);
            startpage = 1;
            ben_data.clear();
            loadData();
        } else {
            listView.setVisibility(View.GONE);
            info_data_view.setVisibility(View.GONE);
            relative_logo.setVisibility(View.VISIBLE);
        }
    }

    public boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result;
    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("startpage", startpage + "");
        params.addQueryStringParameter("pagecount",   "10");
        String urls = String.format(Url.collectList, GetBenSharedPreferences.getTicket(getActivity()));
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.e("collectList", responseInfo.result);
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        title_head.setVisibility(View.VISIBLE);
                        String number = object.getString("number");
                        title_head.setText("已收藏" + number + "条信息");
                        startpage += 1 ;
                        JSONArray data = object.getJSONArray("data");
                        if (data.size() != 0 ){
                            List<HomeEntity> list = JSON.parseArray(data.toJSONString(), HomeEntity.class);
                            ben_data.addAll(list);
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
                                    boolean enable = false;
                                    if(listView != null && listView.getChildCount() > 0){
                                        // check if the first item of the list is visible
                                        boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                                        // check if the top of the first item is visible
                                        boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                                        // enabling or disabling the refresh layout
                                        enable = firstItemVisible && topOfFirstItemVisible;
                                    }
                                    swipeRefreshLayout.setEnabled(enable);
                                }});
                        }else {
                            ToastUtils.shortToast(getActivity() , "没有更多数据");
                        }

                        break;
                    case "400":
                        if (startpage == 1) {
                            title_head.setVisibility(View.GONE);
                            info_data_view.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            if (ben_data != null) {
                                ben_data.clear();
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtils.shortToast(getActivity() , "没有更多数据");
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
        });
    }

}
