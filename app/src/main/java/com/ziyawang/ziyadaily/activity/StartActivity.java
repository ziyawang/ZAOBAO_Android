package com.ziyawang.ziyadaily.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.application.MyApplication;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends Activity implements View.OnClickListener {

    private SharedPreferences sp;
    private MyApplication app;
    private int recLen = 3;
    Timer timer = new Timer();
    private TextView start_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        this.getApplication();
        app.addActivity(this);
        setContentView(R.layout.activity_start);
        start_time = (TextView) findViewById(R.id.start_time);
        start_time.setOnClickListener(this);
        timer.schedule(task, 1000, 1000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recLen--;
                    start_time.setText(recLen + "s");
                    if (recLen == 1) {
                        timer.cancel();
                        changeActivity();
                    }
                }
            });

        }
    };

    private void changeActivity() {
        sp = getSharedPreferences("isFirst", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst", true);
        goSelect(isFirst);

    }

    private void goSelect(boolean isFirst) {
        Intent intent;
        if (isFirst) {
            sp.edit().putBoolean("isFirst", false).commit();
            intent = new Intent(StartActivity.this, WelcomeActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_time :
                changeActivity();
                timer.cancel();
                break;
        }
    }
}
