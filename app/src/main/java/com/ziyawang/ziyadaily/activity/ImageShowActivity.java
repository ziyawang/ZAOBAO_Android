package com.ziyawang.ziyadaily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.mob.tools.utils.BitmapHelper;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.tools.BitmapHelp;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageShowActivity extends BenBenActivity {

    private PhotoView image_pic ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_image_show);
    }

    @Override
    public void initViews() {
        image_pic = (PhotoView )findViewById(R.id.image_pic ) ;
    }

    @Override
    public void initListeners() {
        image_pic.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        String pic = intent.getStringExtra("pic");
        Log.e("benben" , pic ) ;
        BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(ImageShowActivity.this);
        bitmapUtils.display(image_pic , pic );
    }

}
