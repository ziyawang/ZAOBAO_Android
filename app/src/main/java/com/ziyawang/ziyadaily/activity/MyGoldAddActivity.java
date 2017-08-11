package com.ziyawang.ziyadaily.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.adapter.RechargeTypeAdapter;
import com.ziyawang.ziyadaily.entity.RechargeTypeEntity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.Json_RechargeType;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyGridView;
import com.ziyawang.ziyadaily.views.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MyGoldAddActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private TextView common_title ;
    private TextView common_text ;
    private TextView text_account ;

    private static String YOUR_URL = Url.PayCharge ;

    public static final String URL = YOUR_URL;
    private MyGridView gridView ;
    List<RechargeTypeEntity> list ;
    RechargeTypeAdapter adapter ;


    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付宝支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    //微信支付选择区域
    private RelativeLayout wx_relative ;
    //微信支付选择提示框
    private ImageView wx_select ;
    //支付宝支付选择区域
    private RelativeLayout alipay_relative ;
    //支付宝支付选择提示框
    private ImageView alipay_select ;
    //银联支付选择区域
    private RelativeLayout upacp_relative ;
    //银联支付选择提示框
    private ImageView upacp_select ;
    //确认支付按钮
    private Button recharge ;
    //充值的金额
    private int money = 1000;
    //充值的芽币
    private String ybcount = "100" ;
    //充值的渠道
    private String channel = CHANNEL_WECHAT ;
    //数据加载的dialog
    private MyProgressDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载数据
        loadData() ;
    }
    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(MyGoldAddActivity.this, "数据获取中。。。");
        // 不可以用“返回键”取消
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 隐藏数据加载框
     */
    private void hiddenBenDialog() {
        //关闭dialog
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void loadData() {
        //使dialog可见
        showBenDialog();
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.RechargeType, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //关闭dialog
                hiddenBenDialog();
                Log.e("RechargeType", responseInfo.result);
                try {
                    list = Json_RechargeType.getParse(responseInfo.result);
                    for (int i = 0; i < list.size(); i++) {
                        if ("1".equals(list.get(i).getSelected())) {
                            money = Integer.parseInt(list.get(i).getRealMoney());
                            ybcount = list.get(i).getYBCount();
                            recharge.setText("确认支付  ￥" + Integer.parseInt(list.get(i).getRealMoney()) / 100);
                            break;
                        }
                    }
                    adapter = new RechargeTypeAdapter(MyGoldAddActivity.this, list);
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).setSelected("0");
                            }
                            list.get(position).setSelected("1");
                            money = Integer.parseInt(list.get(position).getRealMoney());
                            ybcount = list.get(position).getYBCount();
                            recharge.setText("确认支付  ￥" + Integer.parseInt(list.get(position).getRealMoney()) / 100);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //关闭dialog
                hiddenBenDialog();
                error.printStackTrace();
                ToastUtils.shortToast(MyGoldAddActivity.this, "网络连接异常");
            }
        }) ;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my_gold_add);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        common_text = (TextView)findViewById(R.id.common_text ) ;
        common_title = (TextView)findViewById(R.id.common_title) ;
        text_account = (TextView)findViewById(R.id.text_account) ;

        wx_relative = (RelativeLayout)findViewById(R.id.wx_relative ) ;
        alipay_relative = (RelativeLayout)findViewById(R.id.alipay_relative ) ;
        upacp_relative = (RelativeLayout)findViewById(R.id.upacp_relative ) ;
        upacp_select = (ImageView)findViewById(R.id.upacp_select ) ;
        alipay_select = (ImageView)findViewById(R.id.alipay_select ) ;
        wx_select = (ImageView)findViewById(R.id.wx_select ) ;
        gridView = (MyGridView) findViewById(R.id.gridView);
        recharge = (Button) findViewById(R.id.recharge);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        common_text.setOnClickListener(this);

        recharge.setOnClickListener(this);
        wx_relative.setOnClickListener(this);
        alipay_relative.setOnClickListener(this);
        upacp_relative.setOnClickListener(this);
    }

    @Override
    public void initData() {
        common_title.setText("芽币充值");
        common_text.setText("充值记录");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.common_text:
                Intent intent = new Intent(MyGoldAddActivity.this , GoldRecordActivity.class ) ;
                startActivity(intent);
                break;
            case R.id.wx_relative :
                select(wx_select ,CHANNEL_WECHAT ) ;
                break;
            case R.id.alipay_relative :
                select(alipay_select , CHANNEL_ALIPAY ) ;
                break;
            case R.id.upacp_relative :
                select(upacp_select , CHANNEL_UPACP) ;
                break;
            case R.id.recharge :
                goRecharge() ;
                break;
            default:
                break;
        }
    }

    private void goRecharge() {
        //充值的金额money , 充值的渠道channel
        Log.e("benben", "渠道:" + channel + "---" + "金额：" + money) ;
        //付款
        new PaymentTask().execute(new PaymentRequest(channel, money ));

    }

    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        @Override
        protected void onPreExecute() {
            //按键点击之后的禁用，防止重复点击
            recharge.setOnClickListener(null);
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {

            PaymentRequest paymentRequest = pr[0];
            String data = null;
            String json = new Gson().toJson(paymentRequest);
            try {
                //向Your Ping++ Server SDK请求数据
                data = postJson(URL, json , MyGoldAddActivity.this );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if(null == data){
                showMsg("请求出错", "请检查URL", "URL无法获取charge");
                ToastUtils.shortToast(MyGoldAddActivity.this  , "请检查网络连接");
                return;
            }
            Log.e("charge", data);
            Pingpp.createPayment(MyGoldAddActivity.this, data);
        }

    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //重新获得点击事件
        recharge.setOnClickListener(this);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */

                //Log.e("niubenben" , data.getExtras().toString() ) ;
                //String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                //String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                //showMsg(result, errorMsg, extraMsg);
                if ("success".equals(result)){
                    ToastUtils.shortToast(this , "支付成功！");
                }else if ("fail".equals(result)){
                    ToastUtils.shortToast(this , "支付失败！");
                }else  if ("cancel".equals(result)){
                    ToastUtils.shortToast(this , "取消支付！");
                }else if ("invalid".equals(result)){
                    //ToastUtils.shortToast(this , "该支付方式正在开发，请您选择其他支付方式");
                    ToastUtils.shortToast(this , "未安装该客户端");
                }

            }
        }
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null !=msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null !=msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private static String postJson(String url, String json , Context context) throws IOException {

        String urls = String.format(url, GetBenSharedPreferences.getTicket(context));
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(urls).post(body).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    class PaymentRequest {
        String channel;
        int amount;

        public PaymentRequest(String channel, int amount  ) {
            this.channel = channel;
            this.amount = amount;
        }
    }

    private void select(ImageView v , String str) {
        wx_select.setImageResource(R.mipmap.uncheck);
        alipay_select.setImageResource(R.mipmap.uncheck);
        upacp_select.setImageResource(R.mipmap.uncheck);
        v.setImageResource(R.mipmap.select);
        //选择支付渠道
        channel = str ;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData01();
    }

    private void loadData01() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        String urls = String.format(Url.auth, GetBenSharedPreferences.getTicket(MyGoldAddActivity.this)) ;
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
                            String account = data.getJSONObject(0).getString("Account");
                            text_account.setText(account);
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
                ToastUtils.shortToast( MyGoldAddActivity.this, "网络连接异常");
            }
        }) ;
    }
}
