package com.ziyawang.ziyadaily.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.adapter.MessageAdapter;
import com.ziyawang.ziyadaily.entity.MessageEntity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.CustomDialog;
import com.ziyawang.ziyadaily.views.JustifyTextView;

import org.json.JSONException;

import java.io.File;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class DetailsDailyActivity extends BenBenActivity implements View.OnClickListener {

    private String id;
    private RelativeLayout pre;
    private TextView common_title;

    //private TextView text_01 , text_02 , text_03 , text_04 ;
    private ImageView image_collect, image_share;

    private MessageAdapter adapter;
    private ListView listView;
    //private TextView info_data_view ;
    //private LinearLayout bottom_linear ;
    private String phoneNumber;

    private String title;
    private String content;
    private ScrollView scrollView;
    private LinearLayout headLinearLayout;

    private JustifyTextView des;
    private RelativeLayout relative_des;
    private TextView line;
    private ImageView pictureDet;
    private LinearLayout relative_pictureDet;

    private JustifyTextView des01;
    private RelativeLayout relative_des01;
    private TextView line01;
    private ImageView pictureDet01;
    private LinearLayout relative_pictureDet01;

    private WebView detContent01, detContent;

    private TextView time;
    private JustifyTextView text_title;
    private TextView time01;
    private JustifyTextView text_title01;
    private String detContent_string;

    private EditText edit_des01, edit_des02;
    private TextView text_send01, text_send02;

    private String home_type;

    private LinearLayout linear_bottom;
    private RelativeLayout relative_call, relative_download;
    private TextView text_call, text_download;

    private String account ;
    private String loadPrice;
    private String price;
    private String downLoadUrl;

    private String path;
    private String pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData01();
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
        linear_bottom = (LinearLayout) findViewById(R.id.linear_bottom);
        relative_call = (RelativeLayout) findViewById(R.id.relative_call);
        relative_download = (RelativeLayout) findViewById(R.id.relative_download);
        text_call = (TextView) findViewById(R.id.text_call);
        text_download = (TextView) findViewById(R.id.text_download);

        headLinearLayout = (LinearLayout) LayoutInflater.from(DetailsDailyActivity.this).inflate(R.layout.details_head, null);

        relative_des = (RelativeLayout) headLinearLayout.findViewById(R.id.relative_des);
        detContent01 = (WebView) headLinearLayout.findViewById(R.id.detContent01);
        detContent = (WebView) findViewById(R.id.detContent);
        des = (JustifyTextView) headLinearLayout.findViewById(R.id.des);
        pictureDet = (ImageView) headLinearLayout.findViewById(R.id.pictureDet);
        relative_pictureDet = (LinearLayout) headLinearLayout.findViewById(R.id.relative_pictureDet);
        line = (TextView) headLinearLayout.findViewById(R.id.line);
        time = (TextView) headLinearLayout.findViewById(R.id.time);
        text_title = (JustifyTextView) headLinearLayout.findViewById(R.id.title);

        pre = (RelativeLayout) findViewById(R.id.pre);
        common_title = (TextView) findViewById(R.id.common_title);
        relative_des01 = (RelativeLayout) findViewById(R.id.relative_des);
        des01 = (JustifyTextView) findViewById(R.id.des);
        pictureDet01 = (ImageView) findViewById(R.id.pictureDet);
        relative_pictureDet01 = (LinearLayout) findViewById(R.id.relative_pictureDet);
        line01 = (TextView) findViewById(R.id.line);

        //info_data_view = (TextView) findViewById(R.id.info_data_view ) ;
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        image_collect = (ImageView) findViewById(R.id.image_collect);
        image_share = (ImageView) findViewById(R.id.image_share);
        time01 = (TextView) findViewById(R.id.time);
        text_title01 = (JustifyTextView) findViewById(R.id.title);

        listView = (ListView) findViewById(R.id.listView);
        listView.addHeaderView(headLinearLayout);

        text_send01 = (TextView) findViewById(R.id.text_send01);
        text_send02 = (TextView) findViewById(R.id.text_send02);
        edit_des01 = (EditText) findViewById(R.id.edit_des01);
        edit_des02 = (EditText) findViewById(R.id.edit_des02);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        image_collect.setOnClickListener(this);
        image_share.setOnClickListener(this);
        text_send01.setOnClickListener(this);
        text_send02.setOnClickListener(this);
        relative_download.setOnClickListener(this);
        relative_call.setOnClickListener(this);

        edit_des01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    text_send01.setSelected(true);
                } else {
                    text_send01.setSelected(false);
                }
            }
        });
        edit_des02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    text_send02.setSelected(true);
                } else {
                    text_send02.setSelected(false);
                }
            }
        });
        edit_des01.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    sendComments(edit_des01);
                }
                return false;
            }

        });
        edit_des02.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    sendComments(edit_des02);
                }
                return false;
            }

        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre:
                finish();
                break;
            case R.id.image_collect:
                if (GetBenSharedPreferences.getIsLogin(DetailsDailyActivity.this)) {
                    loadCollectData(id, image_collect);
                } else {
                    goLoginActivity();
                }
                break;
            case R.id.image_share:
                showShare();
                break;
            case R.id.text_send01:
                sendComments(edit_des01);
                break;
            case R.id.text_send02:
                sendComments(edit_des02);
                break;
            case R.id.relative_call:
                if (!GetBenSharedPreferences.getIsLogin(DetailsDailyActivity.this)) {
                    goLoginActivity();
                } else if ("0".equals(price)) {
                    gocall();
                } else {
                    showCustomDialog(price, "call");
                }
                break;
            case R.id.relative_download:
                if (!GetBenSharedPreferences.getIsLogin(DetailsDailyActivity.this)) {
                    goLoginActivity();
                }else if (TextUtils.isEmpty(downLoadUrl)){
                    ToastUtils.shortToast(DetailsDailyActivity.this , "该条信息暂未提供相关资料下载");
                }else if ("0".equals(loadPrice)){
                    goDownload() ;
                }else{
                    showCustomDialog(loadPrice , "downLoad");
                }
                break;
            default:
                break;

        }
    }

    private void goDownload() {
        if (text_download.getText().toString().equals("下载成功,点击打开")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);//Intent.ACTION_VIEW = "android.intent.action.VIEW"
            intent.addCategory(Intent.CATEGORY_DEFAULT);//Intent.CATEGORY_DEFAULT = "android.intent.category.DEFAULT"
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(new File(path));
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        } else {
            download();
        }
    }

    private void loadAccount(final String price, final String type) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        Log.e("token", GetBenSharedPreferences.getTicket(DetailsDailyActivity.this));
        String urls = String.format(Url.auth, GetBenSharedPreferences.getTicket(DetailsDailyActivity.this));
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("auth", responseInfo.result);
                try {
                    org.json.JSONObject object = new org.json.JSONObject(responseInfo.result);
                    String status_code = object.getString("status_code");
                    switch (status_code) {
                        case "200":
                            org.json.JSONArray data = object.getJSONArray("data");
                            account = data.getJSONObject(0).getString("Account");
                            showPopUpWindow(price, type);
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(DetailsDailyActivity.this, "网络连接异常");
            }
        });
    }

    private void showCustomDialog(String price, String type) {
        if ("call".equals(type) && ("1".equals(pay) || "3".equals(pay))) {
            //已经支付过
            gocall();
        } else if ("downLoad".equals(type) && ("2".equals(pay) || "3".equals(pay))){
            goDownload();
        }else {
            loadAccount(price , type);

        }

    }

    private void showPopUpWindow(String price, final String type) {
        //利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_publish, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.relative);

        final TextView info_title = (TextView) view.findViewById(R.id.info_title);

        final TextView shejian_price = (TextView) view.findViewById(R.id.shejian_price);
        final TextView shejian_balance = (TextView) view.findViewById(R.id.shejian_balance);
        //余额不足
        final TextView balance_type = (TextView) view.findViewById(R.id.balance_type);

        final Button shejian_pay = (Button) view.findViewById(R.id.shejian_pay);
        final Button shejian_recharge = (Button) view.findViewById(R.id.shejian_recharge);

        final ImageButton pay_cancel = (ImageButton) view.findViewById(R.id.pay_cancel);

        TextPaint tp = shejian_price.getPaint();
        tp.setFakeBoldText(true);
        TextPaint tp01 = shejian_balance.getPaint();
        tp01.setFakeBoldText(true);

        //消费
        shejian_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去消费
                goPay(window, type);
            }
        });
        //充值
        shejian_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到充值页面
                goRechargeActivity(window);
            }
        });
        if ("call".equals(type)) {
            info_title.setText("消耗芽币可查看联系方式");
        } else {
            info_title.setText("消耗芽币可下载详细资料");
        }
        shejian_price.setText(price);
        shejian_balance.setText(account);
        if (Integer.parseInt(account) < Integer.parseInt(price)) {
            balance_type.setVisibility(View.VISIBLE);
        } else {
            balance_type.setVisibility(View.GONE);
        }

        pay_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(relative, Gravity.CENTER, 0, 0);
        // 设置popWindow的显示和消失动画

        backgroundAlpha(0.2f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }

    private void goPay(final PopupWindow window, final String type) {
        String url = String.format(Url.Pay, GetBenSharedPreferences.getTicket(this));
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("ProjectID", id);
        if ("call".equals(type)){
            params.addBodyParameter("talkType", "talk");
        }else {
            params.addBodyParameter("talkType", "downLoad");
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("Pay", responseInfo.result);
                window.dismiss();
                org.json.JSONObject jsonObject = null;
                try {
                    jsonObject = new org.json.JSONObject(responseInfo.result);
                    String status_code = jsonObject.getString("status_code");
                    switch (status_code) {
                        case "200":
                        case "416":
                        case "417":
                            ToastUtils.shortToast(DetailsDailyActivity.this , "购买成功");
                            if ("call".equals(type)){
                                if ("2".equals(pay) || "3".equals(pay)){
                                    pay = "3" ;
                                }else {
                                    pay = "1" ;
                                }
                                gocall();
                            }else if ("downLoad".equals(type)){
                                if ("1".equals(pay) || "3".equals(pay)){
                                    pay = "3" ;
                                }else {
                                    pay = "2" ;
                                }
                                goDownload();
                            }
                            break;
                        case "418":
                            ToastUtils.shortToast(DetailsDailyActivity.this, "余额不足，请充值。");
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                window.dismiss();
                error.printStackTrace();
                ToastUtils.shortToast(DetailsDailyActivity.this, "网络连接异常");
            }
        });
    }

    private void goRechargeActivity(PopupWindow window) {
        Intent intent = new Intent(DetailsDailyActivity.this, MyGoldAddActivity.class);
        startActivity(intent);
        window.dismiss();
    }

    private void goCallNumber() {
        String str = "tel:" + phoneNumber;
        //直接拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
        if (ActivityCompat.checkSelfPermission(DetailsDailyActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(DetailsDailyActivity.this, "请在管理中心，给予直接拨打电话权限。");
            return;
        }
        startActivity(intent);
    }

    private void download() {
        final long ben = System.currentTimeMillis();
        path = "/sdcard/" + ben + ".pdf";
        HttpUtils http = new HttpUtils();
        http.download(Url.FileIP + downLoadUrl, "/sdcard/" + ben + ".pdf", true, true, new RequestCallBack<File>() {

            @Override
            public void onStart() {
                text_download.setText("正在连接...");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                text_download.setText(current + "/" + total);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if ("maybe the file has downloaded completely".equals(msg)) {
                    text_download.setText("文件位置：/sdcard/" + ben + ".pdf");
                    ToastUtils.shortToast(DetailsDailyActivity.this, "文件已下载");
                }
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                // TODO Auto-generated method stub
                //text_download.setText("文件位置：" + responseInfo.result.getPath());
                ToastUtils.shortToast(DetailsDailyActivity.this, "文件下载成功");
                text_download.setText("下载成功,点击打开");
            }
        });

    }

    private void goLoginActivity() {
        Intent intent = new Intent(DetailsDailyActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void sendComments(final EditText editText) {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
            String urls;
            if (null == GetBenSharedPreferences.getTicket(DetailsDailyActivity.this)) {
                urls = String.format(Url.sendMessage, "");
            } else {
                urls = String.format(Url.sendMessage, GetBenSharedPreferences.getTicket(DetailsDailyActivity.this));
            }
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("projectId", id);
            params.addBodyParameter("content", editText.getText().toString().trim());
            httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("sendMessage", responseInfo.result);
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(responseInfo.result);
                        String status_code = jsonObject.getString("status_code");
                        switch (status_code) {
                            case "200":
                                ToastUtils.shortToast(DetailsDailyActivity.this, "评论发表成功");
                                loadData01();
                                editText.clearFocus();
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
            });
        } else {
            ToastUtils.shortToast(DetailsDailyActivity.this, "请输入您的评论");
        }
    }

//      v1版本功能。暂时废弃
//    private void showCommitWindow() {
//        //scrollView.smoothScrollTo(0, relative_des.getHeight() + relative_pictureDet.getHeight() + line.getHeight());
//        // 利用layoutInflater获得View
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.popupwindow_comment, null);
//        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        final EditText editText = (EditText)view.findViewById(R.id.editText);
//        final TextView text_commit = (TextView)view.findViewById(R.id.text_commit ) ;
//        text_commit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendComments(editText, window);
//            }
//        });
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            public void run() {
//                InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.showSoftInput(editText, 0);
//            }
//
//        }, 100);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s.toString())){
//                    text_commit.setSelected(true);
//                }else {
//                    text_commit.setSelected(false);
//                }
//            }
//        });
//        //点击空白的地方关闭PopupWindow
//        window.setBackgroundDrawable(new BitmapDrawable());
//        // 设置popWindow的显示和消失动画
//        window.setAnimationStyle(R.style.mypopwindow_anim_style);
//        // 在底部显示
//        window.setFocusable(true);
//        window.showAtLocation(bottom_linear, Gravity.BOTTOM, 0, 0);
//        backgroundAlpha(0.5f);
//        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(1f);
//            }
//        });
//    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private void loadCollectData(String id, final ImageView v) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("projectId", id);
        String urls = String.format(Url.collect, GetBenSharedPreferences.getTicket(DetailsDailyActivity.this));
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("collect", responseInfo.result);
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        String msg = object.getString("success_msg");
                        switch (msg) {
                            case "取消收藏成功":
//                                GoodView goodView01 = new GoodView(DetailsDailyActivity.this);
//                                goodView01.setTextInfo("取消收藏" , Color.rgb(153,153,153) , 10 );
//                                goodView01.show(v);
                                //未收藏
                                v.setImageResource(R.mipmap.xqshoucang);
                                break;
                            case "收藏成功":
//                                GoodView goodView = new GoodView(DetailsDailyActivity.this);
//                                goodView.setTextInfo("收藏成功" , Color.rgb(255,77,77) , 10 );
//                                goodView.show(v);
                                //收藏
                                v.setImageResource(R.mipmap.xq_yishoucang);
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
        });
    }

    private void showShare() {
        ShareSDK.initSDK(DetailsDailyActivity.this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle("【资芽早报】" + title);
        oks.setTitleUrl(Url.ShareInfo + id);
        oks.setImageUrl("http://images.ziyawang.com/news/ziyaPaper.png");
        oks.setText(content);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareInfo + id);
        // 启动分享GUI
        oks.show(DetailsDailyActivity.this);
    }

    private void gocall() {
        final CustomDialog.Builder builder01 = new CustomDialog.Builder(DetailsDailyActivity.this);
        builder01.setTitle("亲爱的用户");
        builder01.setMessage("您确定要拨打相关电话?");
        builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goCallNumber();
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


    private void loadData() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        httpUtils.configCurrentHttpCacheExpiry(1000);
        String urls;
        if (null == GetBenSharedPreferences.getTicket(DetailsDailyActivity.this)) {
            urls = String.format(Url.details, id, "");
        } else {
            urls = String.format(Url.details, id, GetBenSharedPreferences.getTicket(DetailsDailyActivity.this));
        }
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("details", responseInfo.result);
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "200":
                        JSONArray data = object.getJSONArray("data");
                        title = data.getJSONObject(0).getString("title");
                        content = data.getJSONObject(0).getString("content");
                        detContent_string = data.getJSONObject(0).getString("detContent");
                        String status = data.getJSONObject(0).getString("status");
                        phoneNumber = data.getJSONObject(0).getString("phoneNumber");
                        price = data.getJSONObject(0).getString("price");
                        loadPrice = data.getJSONObject(0).getString("loadPrice");
                        downLoadUrl = data.getJSONObject(0).getString("downLoad");
                        pay = data.getJSONObject(0).getString("pay");
                        if (!TextUtils.isEmpty(data.getJSONObject(0).getString("label"))) {
                            home_type = data.getJSONObject(0).getString("label");
                            switch (home_type) {
                                case "1":
                                    common_title.setText("转让");
                                    text_call.setText(price + "芽币");
                                    text_download.setText(loadPrice + "芽币");
                                    relative_download.setVisibility(View.VISIBLE);
                                    relative_call.setVisibility(View.VISIBLE);
                                    linear_bottom.setVisibility(View.VISIBLE);
                                    break;
                                case "2":
                                    common_title.setText("求购");
                                    text_call.setText(price + "芽币");
                                    relative_download.setVisibility(View.GONE);
                                    relative_call.setVisibility(View.VISIBLE);
                                    linear_bottom.setVisibility(View.VISIBLE);
                                    break;
                                case "3":
                                    common_title.setText("服务");
                                    text_call.setText(price + "芽币");
                                    relative_download.setVisibility(View.GONE);
                                    relative_call.setVisibility(View.VISIBLE);
                                    linear_bottom.setVisibility(View.VISIBLE);
                                    break;
                                case "4":
                                    common_title.setText("融资");
                                    text_call.setText(price + "芽币");
                                    text_download.setText(loadPrice + "芽币");
                                    relative_download.setVisibility(View.VISIBLE);
                                    relative_call.setVisibility(View.VISIBLE);
                                    linear_bottom.setVisibility(View.VISIBLE);
                                    break;
                                case "5":
                                    common_title.setText("新闻");
                                    linear_bottom.setVisibility(View.GONE);
                                    break;
                                default:
                                    ToastUtils.shortToast(DetailsDailyActivity.this, "label => " + home_type);
                                    break;
                            }
                        } else {
                            ToastUtils.shortToast(DetailsDailyActivity.this, "label null error");
                        }

                        if ("1".equals(status)) {
                            //收藏
                            image_collect.setImageResource(R.mipmap.xq_yishoucang);
                        } else {
                            //未收藏
                            image_collect.setImageResource(R.mipmap.xqshoucang);
                        }

                        final String pictureDet_str = data.getJSONObject(0).getString("describe");
                        if (!TextUtils.isEmpty(pictureDet_str)) {
                            BitmapUtils bitmapUtils1 = new BitmapUtils(DetailsDailyActivity.this);
                            bitmapUtils1.configDefaultLoadFailedImage(R.mipmap.error_imgs);
                            bitmapUtils1.display(pictureDet, Url.FileIP + pictureDet_str);
                            relative_pictureDet.setVisibility(View.VISIBLE);
                            pictureDet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DetailsDailyActivity.this, ImageShowActivity.class);
                                    intent.putExtra("pic", Url.FileIP + pictureDet_str);
                                    startActivity(intent);
                                }
                            });

                            BitmapUtils bitmapUtils2 = new BitmapUtils(DetailsDailyActivity.this);
                            bitmapUtils2.configDefaultLoadFailedImage(R.mipmap.error_imgs);
                            bitmapUtils2.display(pictureDet01, Url.FileIP + pictureDet_str);
                            relative_pictureDet01.setVisibility(View.VISIBLE);
                            pictureDet01.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DetailsDailyActivity.this, ImageShowActivity.class);
                                    intent.putExtra("pic", Url.FileIP + pictureDet_str);
                                    startActivity(intent);
                                }
                            });


                        }

                        des.setText(content);
                        des01.setText(content);
                        time.setText(data.getJSONObject(0).getString("created_at").substring(0, 10));
                        time01.setText(data.getJSONObject(0).getString("created_at").substring(0, 10));
                        text_title.setText(title);
                        text_title01.setText(title);

                        String html = "<head><style>body{text-align:justify;max-width:94%important;margin:0 auto;overflow:hidden!important;}img{max-width:320px !important;height:auto!important;margin:0 auto;width:100%!important;display:block;}</style></head>" + "<body>" + detContent_string + "</body>";
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
        });
    }

    private void loadData01() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("pagecount", "10000");
        httpUtils.configCurrentHttpCacheExpiry(1000);
        params.addQueryStringParameter("projectId", id);
        String urls = String.format(Url.getMessage, GetBenSharedPreferences.getTicket(DetailsDailyActivity.this));
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("getMessage", responseInfo.result);
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code) {
                    case "400":
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
                        adapter = new MessageAdapter(DetailsDailyActivity.this, list);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //scrollView.smoothScrollTo(0, relative_des.getHeight() + relative_pictureDet.getHeight() + line.getHeight());
                        listView.scrollTo(0,0);

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
        });
    }

}
