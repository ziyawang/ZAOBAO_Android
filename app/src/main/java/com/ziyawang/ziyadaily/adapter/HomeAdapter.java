package com.ziyawang.ziyadaily.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wx.goodview.GoodView;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.activity.DetailsDailyActivity;
import com.ziyawang.ziyadaily.activity.LoginActivity;
import com.ziyawang.ziyadaily.entity.HomeEntity;
import com.ziyawang.ziyadaily.tools.GetBenSharedPreferences;
import com.ziyawang.ziyadaily.tools.TimeChange;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.CustomDialog;

import java.util.Collection;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by 牛海丰 on 2017/6/13.
 */

public class HomeAdapter extends BaseAdapter {
    private Context context;
    private List<HomeEntity> list;

    public HomeAdapter() {
    }

    public HomeAdapter(Context context, List<HomeEntity> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_items, parent, false);
            holder = new ViewHolder();
            holder.image_type = (ImageView) convertView.findViewById(R.id.image_type);
            holder.text_type = (TextView) convertView.findViewById(R.id.text_type);
            holder.text_time = (TextView) convertView.findViewById(R.id.text_time);
            holder.des = (TextView) convertView.findViewById(R.id.des);
            holder.relative_01 = (RelativeLayout) convertView.findViewById(R.id.relative_01);
            holder.relative_02 = (RelativeLayout) convertView.findViewById(R.id.relative_02);
            holder.relative_03 = (RelativeLayout) convertView.findViewById(R.id.relative_03);
            holder.relative_04 = (RelativeLayout) convertView.findViewById(R.id.relative_04);
            holder.image_03 = (ImageView) convertView.findViewById(R.id.image_03);
            holder.text_03 = (TextView) convertView.findViewById(R.id.text_03);
            holder.time_title = (TextView) convertView.findViewById(R.id.time_title);
            holder.black = (TextView) convertView.findViewById(R.id.black);
            holder.relative_time_title = (RelativeLayout) convertView.findViewById(R.id.relative_time_title);
            holder.text_share_today = (TextView) convertView.findViewById(R.id.text_share_today);
            holder.relative_mid = (RelativeLayout) convertView.findViewById(R.id.relative_mid);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (list.get(position).getLabel()) {
            case "1":
                holder.image_type.setImageResource(R.mipmap.zhaoxiangmu);
                break;
            case "2":
                holder.image_type.setImageResource(R.mipmap.zichanbao);
                break;
            case "3":
                holder.image_type.setImageResource(R.mipmap.fuwu);
                break;
            case "4":
                holder.image_type.setImageResource(R.mipmap.rongzi);
                break;
            case "5":
                holder.image_type.setImageResource(R.mipmap.zixun);
                break;
            default:
                break;
        }
        holder.text_type.setText(list.get(position).getTitle());
        holder.text_time.setText(list.get(position).getCreated_at().substring(0, 10));
        holder.des.setText(list.get(position).getContent());

        //获取当天你的年月日
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        //String date=sdf.format(new java.util.Date());
        if (position != 0 && list.get(position).getCreated_at().substring(0, 10).equals(list.get(position - 1).getCreated_at().substring(0, 10))) {
            holder.relative_time_title.setVisibility(View.GONE);
            holder.black.setVisibility(View.VISIBLE);
        } else {
            holder.black.setVisibility(View.GONE);
            holder.relative_time_title.setVisibility(View.VISIBLE);
            String week = TimeChange.dateToWeek(list.get(position).getCreated_at().substring(0, 10));
            holder.time_title.setText("资芽早报  " + list.get(position).getCreated_at().substring(0, 10) + "  " + week );
        }
//        switch (list.get(position).getStatus()) {
//            case "0":
//                holder.text_03.setText(R.string.unCollect);
//                holder.text_03.setTextColor(Color.rgb(153, 153, 153));
//                holder.image_03.setImageResource(R.mipmap.shoucang);
//                break;
//            case "1":
//                holder.text_03.setText(R.string.collect);
//                holder.text_03.setTextColor(Color.rgb(255, 77, 77));
//                holder.image_03.setImageResource(R.mipmap.unshoucang);
//                break;
//            default:
//                break;
//        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetailsDailyActivity(position);
            }
        });
//        holder.relative_01.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final CustomDialog.Builder builder01 = new CustomDialog.Builder(context);
//                builder01.setTitle("亲爱的用户");
//                builder01.setMessage("您确定要联系资芽网客服?");
//                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        goCallNumber(list.get(position).getPhoneNumber());
//
//                    }
//                });
//                builder01.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder01.create().show();
//            }
//        });
//        holder.relative_02.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //if (GetBenSharedPreferences.getIsLogin(context)){
//                Intent intent = new Intent(context, DetailsDailyActivity.class);
//                intent.putExtra("id", list.get(position).getProjectId());
//                intent.putExtra("type", "message");
//                context.startActivity(intent);
//                //}else {
//                //    goLoginActivity() ;
//                //}
//            }
//        });
//        holder.relative_03.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (GetBenSharedPreferences.getIsLogin(context)) {
//                    loadData(position, holder.relative_03);
//                } else {
//                    goLoginActivity();
//                }
//            }
//        });
//        holder.relative_04.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showShare(position);
//            }
//        });
        holder.relative_time_title.setOnClickListener(null);
        holder.relative_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(context);
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                String week = TimeChange.dateToWeek(list.get(position).getCreated_at().substring(0, 10));
                oks.setTitle("【资芽早报】资芽网旗下自媒体" +  list.get(position).getCreated_at().substring(0, 10) + "  " + week);
                oks.setTitleUrl(Url.ShareInfoDay + list.get(position).getCreated_at().substring(0, 10));
                oks.setImageUrl("http://images.ziyawang.com/news/ziyaPaper.png");
                oks.setText("【资芽早报】资芽网旗下自媒体" +  list.get(position).getCreated_at().substring(0, 10) + "  " + week);
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl(Url.ShareInfoDay + list.get(position).getCreated_at().substring(0, 10));
                // 启动分享GUI
                oks.show(context);
            }
        });
        return convertView;
    }

    private void goDetailsDailyActivity(int position) {
        Intent intent = new Intent(context, DetailsDailyActivity.class);
        intent.putExtra("id", list.get(position).getProjectId());
        context.startActivity(intent);
    }

    private void goLoginActivity() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void showShare(int position) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle("【资芽早报】" + list.get(position).getTitle());
        oks.setTitleUrl(Url.ShareInfo + list.get(position).getProjectId());
        oks.setImageUrl("http://images.ziyawang.com/news/ziyaPaper.png");
        oks.setText(list.get(position).getContent());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareInfo + list.get(position).getProjectId());
        // 启动分享GUI
        oks.show(context);
    }

    private void goCallNumber(String phoneNumber) {
        String str = "tel:" + phoneNumber;
        //直接拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(context, "请在管理中心，给予直接拨打电话权限。");
            return;
        }
        context.startActivity(intent);
    }

//    private void loadData(final int position, final RelativeLayout v) {
//        HttpUtils httpUtils = new HttpUtils();
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("projectId", list.get(position).getProjectId());
//        String urls = String.format(Url.collect, GetBenSharedPreferences.getTicket(context));
//        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("collect", responseInfo.result);
//                JSONObject object = JSON.parseObject(responseInfo.result);
//                String status_code = object.getString("status_code");
//                switch (status_code) {
//                    case "200":
//                        String msg = object.getString("success_msg");
//                        switch (msg) {
//                            case "取消收藏成功":
//                                GoodView goodView01 = new GoodView(context);
//                                goodView01.setTextInfo("取消收藏", Color.rgb(153, 153, 153), 10);
//                                goodView01.show(v);
//                                list.get(position).setStatus("0");
//                                notifyDataSetChanged();
//                                break;
//                            case "收藏成功":
//                                GoodView goodView = new GoodView(context);
//                                goodView.setTextInfo("收藏成功", Color.rgb(255, 77, 77), 10);
//                                goodView.show(v);
//                                list.get(position).setStatus("1");
//                                notifyDataSetChanged();
//                                break;
//                            default:
//                                break;
//                        }
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                error.printStackTrace();
//                ToastUtils.shortToast(context, "网络连接异常");
//            }
//        });
//    }

    static class ViewHolder {
        ImageView image_type;
        TextView text_type;
        TextView text_time;
        TextView des;
        RelativeLayout relative_01, relative_02, relative_03, relative_04;
        ImageView image_03;
        TextView text_03;
        TextView time_title;
        TextView black ;
        RelativeLayout relative_time_title ;
        TextView text_share_today ;
        RelativeLayout relative_mid ;

    }

    public void addAll(Collection<? extends HomeEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
