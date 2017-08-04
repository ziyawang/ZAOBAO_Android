package com.ziyawang.ziyadaily.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.activity.CustomizationDailyActivity;
import com.ziyawang.ziyadaily.activity.LoginActivity;
import com.ziyawang.ziyadaily.activity.SearchActivity;
import com.ziyawang.ziyadaily.adapter.HomeFragmentPagerAdapter;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;

import java.util.ArrayList;

/**
 * Created by 牛海丰 on 2017/8/1.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private TabLayout tabs ;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private TextView customizationDaily ;

    //无参构造
    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home103, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化组件
        initView(view);
        initListeners() ;
    }

    private void initListeners() {
        customizationDaily.setOnClickListener(this);
    }

    private void initView(View v ) {
        tabs = (TabLayout)v.findViewById(R.id.tabs ) ;
        mViewPager = (ViewPager) v.findViewById(R.id.mViewPager);
        tabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        customizationDaily = (TextView)v.findViewById(R.id.customizationDaily ) ;
        initFragment() ;

    }

    /**
     *  初始化Fragment
     * */
    private void initFragment() {

        Bundle data01 = new Bundle();
        data01.putString("type", "0");
        HomeItemFragment homeItemFragment01 = new HomeItemFragment() ;
        homeItemFragment01.setArguments(data01);

        Bundle data02 = new Bundle();
        data02.putString("type", "1");
        HomeItemFragment homeItemFragment02 = new HomeItemFragment() ;
        homeItemFragment02.setArguments(data02);

        Bundle data03 = new Bundle();
        data03.putString("type", "2");
        HomeItemFragment homeItemFragment03 = new HomeItemFragment() ;
        homeItemFragment03.setArguments(data03);

        Bundle data04 = new Bundle();
        data04.putString("type", "3");
        HomeItemFragment homeItemFragment04 = new HomeItemFragment() ;
        homeItemFragment04.setArguments(data04);

        Bundle data05 = new Bundle();
        data05.putString("type", "4");
        HomeItemFragment homeItemFragment05 = new HomeItemFragment() ;
        homeItemFragment05.setArguments(data05);

        Bundle data06 = new Bundle();
        data06.putString("type", "5");
        HomeItemFragment homeItemFragment06 = new HomeItemFragment() ;
        homeItemFragment06.setArguments(data06);

        fragments.add(homeItemFragment01) ;
        fragments.add(homeItemFragment02) ;
        fragments.add(homeItemFragment03) ;
        fragments.add(homeItemFragment04) ;
        fragments.add(homeItemFragment05) ;
        fragments.add(homeItemFragment06) ;

        final HomeFragmentPagerAdapter mAdapetr = new HomeFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        tabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.customizationDaily :
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    Intent intent = new Intent(getActivity() , SearchActivity.class ) ;
                    startActivity( intent );
                    getActivity().overridePendingTransition(R.anim.in , R.anim.out );
                }else {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent );
                }
                break;
            default:
                break;
        }

    }
}
