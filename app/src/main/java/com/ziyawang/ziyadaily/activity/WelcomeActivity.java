package com.ziyawang.ziyadaily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.adapter.WelcomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends BenBenActivity {

    //存放引导页的ViewPager
    private ViewPager viewPager;
    //viewPager上加载的数据
    private List<ImageView> list;
    //适配器
    private WelcomeAdapter adapter;
    //四张引导图
    private int[] imageIds = new int[]{R.mipmap.ic_launcher ,R.mipmap.ic_launcher ,R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    //引导页对应的引导点点
    private ImageView[] icons = new ImageView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        icons[0] = (ImageView) findViewById(R.id.icon1);
        icons[1] = (ImageView) findViewById(R.id.icon2);
        icons[2] = (ImageView) findViewById(R.id.icon3);
        icons[3] = (ImageView) findViewById(R.id.icon4);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        list = new ArrayList<ImageView>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageIds[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            list.add(imageView);
            icons[i].setEnabled(false);
        }
        icons[0].setEnabled(true);

        adapter = new WelcomeAdapter(this ,list  );
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                select(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void select(int position) {
        for (int i = 0; i < icons.length; i++) {
            icons[i].setEnabled(false);
        }
        icons[position].setEnabled(true);
        if (position == 3) {
            //点击跳转到主页面
            list.get(position).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.get(0).destroyDrawingCache();
        list.get(1).destroyDrawingCache();
        list.get(2).destroyDrawingCache();
        list.get(3).destroyDrawingCache();
        list.clear();
    }
}
