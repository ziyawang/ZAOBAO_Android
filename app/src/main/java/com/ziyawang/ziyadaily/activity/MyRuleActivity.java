package com.ziyawang.ziyadaily.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.tools.Url;

public class MyRuleActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre;
    private WebView webView;
    private ProgressBar bar;
    private TextView info_title ;
    private String web ;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my_rule);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout) findViewById(R.id.pre);
        webView = (WebView) findViewById(R.id.webView);
        bar = (ProgressBar) findViewById(R.id.bar);
        info_title = (TextView)findViewById(R.id.info_title ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        String type = intent.getStringExtra("type");
        if ("rule".equals(type)){
            //WebView加载web资源
            web = Url.Rule ;
            info_title.setText("资芽公约");
        }else if ("other".equals(type)){
            info_title.setText(intent.getStringExtra("title"));
            web = intent.getStringExtra("url");
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData() ;

    }

    private void loadData() {
        webView.loadUrl(web);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        webView.getSettings().setDisplayZoomControls(false);
    }
}
