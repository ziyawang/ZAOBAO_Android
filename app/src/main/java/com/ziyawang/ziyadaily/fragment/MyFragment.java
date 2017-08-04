package com.ziyawang.ziyadaily.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.activity.AboutActivity;
import com.ziyawang.ziyadaily.activity.FeedBackActivity;
import com.ziyawang.ziyadaily.activity.InfoNoticeActivity;
import com.ziyawang.ziyadaily.activity.LoginActivity;
import com.ziyawang.ziyadaily.activity.MyGoldAddActivity;
import com.ziyawang.ziyadaily.activity.MyMessageActivity;
import com.ziyawang.ziyadaily.activity.MyPublishActivity;
import com.ziyawang.ziyadaily.activity.PersonActivity;
import com.ziyawang.ziyadaily.activity.SysSettingActivity;
import com.ziyawang.ziyadaily.tools.BitmapHelp;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.LoadImageAsyncTask;
import com.ziyawang.ziyadaily.tools.SDUtil;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyIconImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;


/**
 * Created by 牛海丰 on 2017/6/12.
 */

public class MyFragment extends Fragment implements View.OnClickListener {

    private TextView common_title ;
    private RelativeLayout relative ;
    private MyIconImageView icon ;
    private TextView userName ;
    private TextView me_infoNotice ;
    private TextView me_MyPublish ;
    private TextView me_MyMessage ;
    private TextView me_FeedBack ;
    private TextView me_SysSetting ;
    private TextView me_AboutUs ;
    private TextView me_gold_add ;

    private String username ;
    private String phonenumber ;
    //头像在内存中的缓存bitmap
    private Bitmap bitmap ;
    //芽币余额
    private TextView text_account ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化组件
        initView(view) ;
        //注册监听事件
        initListeners() ;
        //实例化数据
        initData() ;
    }

    private void initData() {
        common_title.setText(R.string.home_me);
    }

    private void initListeners() {
        relative.setOnClickListener(this);
        me_infoNotice.setOnClickListener(this);
        me_MyPublish.setOnClickListener(this);
        me_MyMessage.setOnClickListener(this);
        me_FeedBack.setOnClickListener(this);
        me_SysSetting.setOnClickListener(this);
        me_AboutUs.setOnClickListener(this);
        me_gold_add.setOnClickListener(this);
    }

    private void initView(View view) {
        common_title = (TextView)view.findViewById(R.id.common_title ) ;
        icon = (MyIconImageView) view.findViewById(R.id.icon ) ;
        userName = (TextView)view.findViewById(R.id.userName ) ;
        me_infoNotice = (TextView)view.findViewById(R.id.me_infoNotice ) ;
        me_MyPublish = (TextView)view.findViewById(R.id.me_MyPublish ) ;
        me_gold_add = (TextView)view.findViewById(R.id.me_gold_add ) ;
        me_MyMessage = (TextView)view.findViewById(R.id.me_MyMessage ) ;
        me_FeedBack = (TextView)view.findViewById(R.id.me_FeedBack ) ;
        me_SysSetting = (TextView)view.findViewById(R.id.me_SysSetting ) ;
        me_AboutUs = (TextView)view.findViewById(R.id.me_AboutUs ) ;
        relative = (RelativeLayout) view.findViewById(R.id.relative ) ;
        text_account = (TextView)view.findViewById(R.id.text_account ) ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative :
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    Intent intent = new Intent(getActivity() , PersonActivity.class ) ;
                    intent.putExtra("phoneNumber" , phonenumber ) ;
                    intent.putExtra("username" , username ) ;
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent);
                }
                break;
            case R.id.me_infoNotice :
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    Intent intent = new Intent(getActivity() , InfoNoticeActivity.class ) ;
                    startActivity(intent);
                }else {
                    goLoginActivity() ;
                }
                break;
            case R.id.me_MyPublish :
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    Intent intent01 = new Intent(getActivity() , MyPublishActivity.class ) ;
                    startActivity(intent01);
                }else {
                    goLoginActivity() ;
                }
                break;
            case R.id.me_MyMessage :
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    Intent intent02 = new Intent(getActivity() , MyMessageActivity.class ) ;
                    startActivity(intent02);
                }else {
                    goLoginActivity() ;
                }
                break;
            case R.id.me_gold_add :
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    Intent intent02 = new Intent(getActivity() , MyGoldAddActivity.class ) ;
                    startActivity(intent02);
                }else {
                    goLoginActivity() ;
                }
                break;
            case R.id.me_FeedBack :
                goFeedBackActivity() ;
                break;
            case R.id.me_SysSetting :
                goSysSettingActivity() ;
                break;
            case R.id.me_AboutUs :
                goAboutActivity() ;
                break;
            default:
                break;
        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
        startActivity( intent );
    }

    private void goAboutActivity() {
        Intent intent = new Intent(getActivity() , AboutActivity.class ) ;
        startActivity(intent);
    }

    private void goSysSettingActivity() {
        Intent intent = new Intent(getActivity() , SysSettingActivity.class ) ;
        startActivity(intent);
    }

    private void goFeedBackActivity() {
        Intent intent = new Intent(getActivity() , FeedBackActivity.class ) ;
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        initIcon() ;
        if (GetBenSharedPreferences.getIsLogin(getActivity())){
            loadData() ;
        }else {
            icon.setImageResource(R.mipmap.icon_big);
            userName.setText(R.string.login_register);
        }
    }

    private void initIcon() {
        if (GetBenSharedPreferences.getIsLogin(getContext())){
            //先拿到用户的缓存的头像,存在进行加载，不能重新操作。
            File files = new File(Url.IconPath);
            if (files.exists()) {
                final byte[] icons = SDUtil.getDataFromSDCard("icon.png");
                bitmap = BitmapFactory.decodeByteArray(icons, 0, icons.length);
                icon.setImageBitmap(bitmap);
            }
        }
    }

    //得到图片的类型
    public String getSubStr(String str){
        int position = str.lastIndexOf(".");
        str = str.substring(position + 1);
        return str;
    }

    //将bitmap转化为byte[]
    public byte[] getByte(Bitmap bit , String str){
        Bitmap.CompressFormat temp = null;
        if ("jpg".equals(str)){
            temp = Bitmap.CompressFormat.JPEG;
        }else if ("png".equals(str)){
            temp = Bitmap.CompressFormat.PNG;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(temp, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        Log.e("token", GetBenSharedPreferences.getTicket(getActivity())) ;
        String urls = String.format(Url.auth, GetBenSharedPreferences.getTicket(getActivity())) ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("auth" , responseInfo.result ) ;
                try {
                    JSONObject object = new JSONObject(responseInfo.result ) ;
                    String status_code = object.getString("status_code");
                    switch (status_code){
                        case "200" :
                            JSONArray data = object.getJSONArray("data");
                            username = data.getJSONObject(0).getString("username");
                            phonenumber = data.getJSONObject(0).getString("phonenumber");
                            final String UserPicture = data.getJSONObject(0).getString("UserPicture");
                            String account = data.getJSONObject(0).getString("Account");
                            new LoadImageAsyncTask(new LoadImageAsyncTask.CallBack() {
                                @Override
                                public void setData(final Bitmap bitmap) {
                                    icon.setImageBitmap(bitmap);
                                    icon.setDrawingCacheEnabled(true);
                                    Bitmap bitmap_icon = Bitmap.createBitmap(icon.getDrawingCache());
                                    String subStr = getSubStr(Url.FileIP + UserPicture);
                                    final byte[] icon_data = getByte(bitmap_icon, subStr);
                                    SDUtil.saveDataInfoSDCard(icon_data, "ziya", "icon.png");
                                }
                            }).execute(Url.FileIP + UserPicture);
                            if ("".equals(username)){
                                userName.setText(phonenumber);
                            }else {
                                userName.setText(username);
                            }
                            text_account.setText("芽币余额： " + account );
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
                ToastUtils.shortToast(getActivity(), "网络连接异常");
            }
        }) ;
    }


}
