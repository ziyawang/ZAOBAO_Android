package com.ziyawang.ziyadaily.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wx.goodview.GoodView;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.adapter.MessageAdapter;
import com.ziyawang.ziyadaily.entity.MessageEntity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.BenListView;
import com.ziyawang.ziyadaily.views.CustomDialog;
import com.ziyawang.ziyadaily.views.JustifyTextView;

import org.json.JSONException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class DetailsDailyActivity extends BenBenActivity implements View.OnClickListener {

    private String id ;
    private RelativeLayout pre ;
    private TextView common_title ;

    private TextView text_01 , text_02 , text_03 , text_04 ;

    private MessageAdapter adapter ;
    private ListView listView ;
    //private TextView info_data_view ;
    private LinearLayout bottom_linear ;
    private String phoneNumber ;

    private String title ;
    private String content ;
    private ScrollView scrollView ;
    private LinearLayout headLinearLayout ;

    private JustifyTextView des ;
    private RelativeLayout relative_des ;
    private TextView line ;
    private ImageView pictureDet ;
    private LinearLayout relative_pictureDet ;

    private JustifyTextView des01 ;
    private RelativeLayout relative_des01 ;
    private TextView line01 ;
    private ImageView pictureDet01 ;
    private LinearLayout relative_pictureDet01 ;

    private WebView detContent01 ,detContent ;

    private TextView time ;
    private JustifyTextView text_title ;
    private TextView time01 ;
    private JustifyTextView text_title01 ;
    private String detContent_string ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData01(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_details_daily);
    }

    @Override
    public void initViews() {
        headLinearLayout = (LinearLayout) LayoutInflater.from(DetailsDailyActivity.this).inflate(R.layout.details_head , null ) ;

        relative_des = (RelativeLayout) headLinearLayout.findViewById(R.id.relative_des ) ;
        detContent01 = (WebView) headLinearLayout.findViewById(R.id.detContent01 ) ;
        detContent = (WebView) findViewById(R.id.detContent ) ;
        des = (JustifyTextView) headLinearLayout.findViewById(R.id.des ) ;
        pictureDet = (ImageView) headLinearLayout.findViewById(R.id.pictureDet ) ;
        relative_pictureDet = (LinearLayout) headLinearLayout.findViewById(R.id.relative_pictureDet ) ;
        line = (TextView) headLinearLayout.findViewById(R.id.line ) ;
        time = (TextView) headLinearLayout.findViewById(R.id.time ) ;
        text_title = (JustifyTextView) headLinearLayout.findViewById(R.id.title ) ;

        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        common_title = (TextView)findViewById(R.id.common_title ) ;
        relative_des01 = (RelativeLayout) findViewById(R.id.relative_des ) ;
        des01 = (JustifyTextView) findViewById(R.id.des ) ;
        pictureDet01 = (ImageView) findViewById(R.id.pictureDet ) ;
        relative_pictureDet01 = (LinearLayout) findViewById(R.id.relative_pictureDet ) ;
        line01 = (TextView) findViewById(R.id.line ) ;
        text_01 = (TextView) findViewById(R.id.text_01 ) ;
        text_02 = (TextView) findViewById(R.id.text_02 ) ;
        text_03 = (TextView) findViewById(R.id.text_03 ) ;
        text_04 = (TextView) findViewById(R.id.text_04 ) ;
        listView = (ListView) findViewById(R.id.listView ) ;
        //info_data_view = (TextView) findViewById(R.id.info_data_view ) ;
        bottom_linear = (LinearLayout) findViewById(R.id.bottom_linear ) ;
        scrollView = (ScrollView)findViewById(R.id.scrollView ) ;

        time01 = (TextView)findViewById(R.id.time ) ;
        text_title01 = (JustifyTextView)findViewById(R.id.title ) ;

        listView.addHeaderView(headLinearLayout);


    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        text_01.setOnClickListener(this);
        text_02.setOnClickListener(this);
        text_03.setOnClickListener(this);
        text_04.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        id = intent.getStringExtra("id");
        if (intent.getStringExtra("type") != null ){
            popupHandler.sendEmptyMessageDelayed(0, 1000);
        }
    }

    private Handler popupHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showCommitWindow();
                    break;
            }
        }

    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.text_01 :
                gocall() ;
                break;
            case R.id.text_02 :
                //if (GetBenSharedPreferences.getIsLogin(DetailsDailyActivity.this)){
                    showCommitWindow() ;
                //}else {
                //    goLoginActivity() ;
                //}

                break;
            case R.id.text_03 :
                if (GetBenSharedPreferences.getIsLogin(DetailsDailyActivity.this)){
                    loadCollectData(id  , text_03) ;
                }else {
                    goLoginActivity() ;
                }
                break;
            case R.id.text_04 :
                showShare() ;
                break;
            default:
                break;

        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(DetailsDailyActivity.this , LoginActivity.class ) ;
        startActivity(intent);
    }

    private void sendComments(EditText editText, final PopupWindow window) {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())){
            String urls ;
            if (null == GetBenSharedPreferences.getTicket(DetailsDailyActivity.this) ){
                urls = String.format(Url.sendMessage , "" ) ;
            }else {
                urls = String.format(Url.sendMessage,GetBenSharedPreferences.getTicket(DetailsDailyActivity.this) ) ;
            }
            HttpUtils httpUtils = new HttpUtils() ;
            RequestParams params = new RequestParams() ;
            params.addBodyParameter("projectId" , id );
            params.addBodyParameter("content" , editText.getText().toString().trim() );
            httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("sendMessage" , responseInfo.result ) ;
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(responseInfo.result) ;
                        String status_code = jsonObject.getString("status_code");
                        switch (status_code){
                            case "200" :
                                ToastUtils.shortToast(DetailsDailyActivity.this, "评论发表成功");
                                loadData01(2);
                                window.dismiss();
                                break;
                            default:
                                break;
                        }
                    } catch (JSONException e) {
                    }
                }
                @Override
                public void onFailure(HttpException error, String msg) {
                    error.printStackTrace();
                    ToastUtils.shortToast(DetailsDailyActivity.this, "网络连接异常");
                }
            }) ;
        }else {
            ToastUtils.shortToast(DetailsDailyActivity.this , "请输入您的评论");
        }
    }

    private void showCommitWindow() {
        //scrollView.smoothScrollTo(0, relative_des.getHeight() + relative_pictureDet.getHeight() + line.getHeight());
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_comment, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final EditText editText = (EditText)view.findViewById(R.id.editText);
        final TextView text_commit = (TextView)view.findViewById(R.id.text_commit ) ;
        text_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComments(editText, window);
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 100);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())){
                    text_commit.setSelected(true);
                }else {
                    text_commit.setSelected(false);
                }
            }
        });
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.setFocusable(true);
        window.showAtLocation(bottom_linear, Gravity.BOTTOM, 0, 0);
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
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private void loadCollectData(String id , final TextView v ) {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("projectId" , id );
        String urls = String.format(Url.collect, GetBenSharedPreferences.getTicket(DetailsDailyActivity.this) ) ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("collect" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        String msg = object.getString("success_msg");
                        switch (msg) {
                            case "取消收藏成功":
                                GoodView goodView01 = new GoodView(DetailsDailyActivity.this);
                                goodView01.setTextInfo("取消收藏" , Color.rgb(153,153,153) , 10 );
                                goodView01.show(v);
                                //未收藏
                                Drawable drawable02 = getResources().getDrawable(R.mipmap.xqshoucang);
                                drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                                text_03.setCompoundDrawables(null, drawable02, null, null);
                                text_03.setTextColor(Color.rgb(153,153,153));
                                break;
                            case "收藏成功":
                                GoodView goodView = new GoodView(DetailsDailyActivity.this);
                                goodView.setTextInfo("收藏成功" , Color.rgb(255,77,77) , 10 );
                                goodView.show(v);
                                //收藏
                                Drawable drawable01 = getResources().getDrawable(R.mipmap.details_collect);
                                drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
                                text_03.setCompoundDrawables(null, drawable01, null, null);
                                text_03.setTextColor(Color.rgb(255,77,77));
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(DetailsDailyActivity.this, "网络连接异常");
            }
        }) ;
    }

    private void showShare() {
        ShareSDK.initSDK(DetailsDailyActivity.this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setTitleUrl(Url.ShareInfo + id );
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png");
        oks.setText(content);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareInfo + id );
        // 启动分享GUI
        oks.show(DetailsDailyActivity.this);
    }

    private void gocall() {
        final CustomDialog.Builder builder01 = new CustomDialog.Builder(DetailsDailyActivity.this);
        builder01.setTitle("亲爱的用户");
        builder01.setMessage("您确定要联系资芽网客服?");
        builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {goCallNumber() ;

            }
        });
        builder01.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder01.create().show();
    }

    private void goCallNumber() {
        String str = "tel:" + phoneNumber ;
        //直接拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
        if (ActivityCompat.checkSelfPermission(DetailsDailyActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(DetailsDailyActivity.this, "请在管理中心，给予直接拨打电话权限。");
            return;
        }
        DetailsDailyActivity.this.startActivity(intent);
    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        String urls ;
        if (null == GetBenSharedPreferences.getTicket(DetailsDailyActivity.this) ){
            urls = String.format(Url.details ,id, "" ) ;
        }else {
            urls = String.format(Url.details,id,GetBenSharedPreferences.getTicket(DetailsDailyActivity.this) ) ;
        }
        httpUtils.send(HttpRequest.HttpMethod.GET, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("details" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        JSONArray data = object.getJSONArray("data");
                        title = data.getJSONObject(0).getString("title");
                        content = data.getJSONObject(0).getString("content");
                        detContent_string = data.getJSONObject(0).getString("detContent");
                        String status = data.getJSONObject(0).getString("status");
                        phoneNumber = data.getJSONObject(0).getString("phoneNumber") ;
                        if ("1".equals(status)){
                            //收藏
                            Drawable drawable02 = getResources().getDrawable(R.mipmap.details_collect);
                            drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                            text_03.setCompoundDrawables(null, drawable02, null, null);
                            text_03.setTextColor(Color.rgb(255,77,77));
                        }else {
                            //未收藏
                            Drawable drawable02 = getResources().getDrawable(R.mipmap.xqshoucang);
                            drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                            text_03.setCompoundDrawables(null, drawable02, null, null);
                            text_03.setTextColor(Color.rgb(153,153,153));
                        }

                        final String pictureDet_str = data.getJSONObject(0).getString("describe");
                        if (!TextUtils.isEmpty(pictureDet_str)){
                            BitmapUtils bitmapUtils1 = new BitmapUtils(DetailsDailyActivity.this);
                            bitmapUtils1.configDefaultLoadFailedImage(R.mipmap.error_imgs);
                            bitmapUtils1.display(pictureDet, Url.FileIP + pictureDet_str );
                            relative_pictureDet.setVisibility(View.VISIBLE);
                            pictureDet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DetailsDailyActivity.this, ImageShowActivity.class);
                                    intent.putExtra("pic" ,  Url.FileIP + pictureDet_str ) ;
                                    startActivity(intent);
                                }
                            });

                            BitmapUtils bitmapUtils2 = new BitmapUtils(DetailsDailyActivity.this);
                            bitmapUtils2.configDefaultLoadFailedImage(R.mipmap.error_imgs);
                            bitmapUtils2.display(pictureDet01, Url.FileIP + pictureDet_str );
                            relative_pictureDet01.setVisibility(View.VISIBLE);
                            pictureDet01.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DetailsDailyActivity.this, ImageShowActivity.class);
                                    intent.putExtra("pic" ,  Url.FileIP + pictureDet_str ) ;
                                    startActivity(intent);
                                }
                            });


                        }
                        String type = data.getJSONObject(0).getString("type");
                        switch (type){
                            case "1" :
                                common_title.setText("资讯");
                                break;
                            case "2" :
                                common_title.setText("找项目");
                                break;
                            case "3" :
                                common_title.setText("出售资产");
                                break;
                            default:
                                break;
                        }

                        des.setText(content);
                        des01.setText(content);
                        time.setText(data.getJSONObject(0).getString("created_at").substring(0 ,10));
                        time01.setText(data.getJSONObject(0).getString("created_at").substring(0 ,10));
                        text_title.setText(title);
                        text_title01.setText(title);

                        String html = "<head><style>body{max-width:94%important;margin:0 auto;overflow:hidden!important;}img{max-width:320px !important;height:auto!important;margin:0 auto;width:100%!important;display:block;}</style></head>" + "<body>" + detContent_string + "</body>";
                        WebSettings ws = detContent.getSettings();
                        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                        detContent.setVerticalScrollBarEnabled(false);
                        detContent.loadDataWithBaseURL(null, html, "text/html", "unicode", null);

                        WebSettings ws01 = detContent01.getSettings();
                        ws01.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                        detContent01.setVerticalScrollBarEnabled(false);
                        detContent01.loadDataWithBaseURL(null, html, "text/html", "unicode", null);



                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(DetailsDailyActivity.this, "网络连接异常");
            }
        }) ;
    }

    private void loadData01(final int type ) {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pagecount" , "10000");
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        params.addQueryStringParameter("projectId" , id );
        String urls = String.format(Url.getMessage, GetBenSharedPreferences.getTicket(DetailsDailyActivity.this)) ;
        httpUtils.send(HttpRequest.HttpMethod.GET,urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("getMessage" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "400" :
                        listView.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        //listView.setVisibility(View.VISIBLE);
                        //info_data_view.setVisibility(View.VISIBLE);
                        break;
                    case "200":
                        listView.setVisibility(View.VISIBLE);
                        //info_data_view.setVisibility(View.GONE);
                        JSONArray data = object.getJSONArray("data");
                        final List<MessageEntity> list = JSON.parseArray(data.toJSONString(), MessageEntity.class);
                        adapter = new MessageAdapter(DetailsDailyActivity.this , list ) ;
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (type == 1 ){
                            //scrollView.scrollTo(0 , 0 );
                        }else {
                            //scrollView.smoothScrollTo(0, relative_des.getHeight() + relative_pictureDet.getHeight() + line.getHeight());
                            listView.smoothScrollToPosition(1);
                        }

                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(DetailsDailyActivity.this, "网络连接异常");
            }
        }) ;
    }
}
