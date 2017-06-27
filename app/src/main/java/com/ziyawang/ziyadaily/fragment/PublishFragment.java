package com.ziyawang.ziyadaily.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.activity.DetailsPublishActivity;
import com.ziyawang.ziyadaily.activity.LoginActivity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;

/**
 * Created by 牛海丰 on 2017/6/12.
 */

public class PublishFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    //private RelativeLayout saleAsset, buyRequired;
    private TextView saleAsset, buyRequired;
    private Animation mButtonScaleLargeAnimation;
    private Animation mButtonScaleSmallAnimation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publish, container, false);
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
        //saleAsset.setOnClickListener(this);
        //buyRequired.setOnClickListener(this);

        saleAsset.setOnTouchListener(this);
        buyRequired.setOnTouchListener(this);
    }

    private void initView(View view) {
        //buyRequired = (RelativeLayout) view.findViewById(R.id.BuyRequired);
        //saleAsset = (RelativeLayout) view.findViewById(R.id.SaleAsset);
        buyRequired = (TextView) view.findViewById(R.id.BuyRequired);
        saleAsset = (TextView) view.findViewById(R.id.SaleAsset);

        mButtonScaleLargeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_scale_to_large);
        mButtonScaleSmallAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_scale_to_small);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.SaleAsset :
//                if (GetBenSharedPreferences.getIsLogin(getActivity())){
//                    goDetailsPublishActivity("1") ;
//                }else {
//                    goLoginActivity() ;
//                }
//                break;
//            case R.id.BuyRequired :
//                if (GetBenSharedPreferences.getIsLogin(getActivity())){
//                    goDetailsPublishActivity("2") ;
//                }else {
//                    goLoginActivity() ;
//                }
//                break;
            default:
                break;
        }
    }

    private void goDetailsPublishActivity(String type) {
        Intent intent = new Intent(getActivity(), DetailsPublishActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void goLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下，按钮执行放大动画
                v.startAnimation(mButtonScaleLargeAnimation);
                break;
            case MotionEvent.ACTION_UP:
                switch (v.getId()) {
                    case R.id.SaleAsset:
                        v.clearAnimation();
                        if (GetBenSharedPreferences.getIsLogin(getActivity())) {
                            goDetailsPublishActivity("1");
                        } else {
                            goLoginActivity();
                        }
                        break;
                    case R.id.BuyRequired:
                        v.clearAnimation();
                        if (GetBenSharedPreferences.getIsLogin(getActivity())) {
                            goDetailsPublishActivity("2");
                        } else {
                            goLoginActivity();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                // 手指移开，按钮执行缩小动画
                v.startAnimation(mButtonScaleSmallAnimation);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 缩小动画执行完毕后，将按钮的动画清除。这里的150毫秒是缩小动画的执行时间。
                        v.clearAnimation();
                    }
                }, 150);
                return true ;
        }
        return true;
    }
}
