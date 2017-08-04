package com.ziyawang.ziyadaily.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.activity.DetailsDailyActivity;
import com.ziyawang.ziyadaily.activity.LoginActivity;
import com.ziyawang.ziyadaily.entity.HomeEntity;
import com.ziyawang.ziyadaily.tools.TimeChange;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;

import java.util.Collection;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by 牛海丰 on 2017/6/13.
 */

public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<HomeEntity> list;

    public SearchAdapter() {
    }

    public SearchAdapter(Context context, List<HomeEntity> list) {
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
            holder.time_title = (TextView) convertView.findViewById(R.id.time_title);
            holder.text_share_today = (TextView) convertView.findViewById(R.id.text_share_today);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (list.get(position).getType()) {
            case "1":
                holder.image_type.setImageResource(R.mipmap.zixun);
                break;
            case "2":
                holder.image_type.setImageResource(R.mipmap.zhaoxiangmu);
                break;
            case "3":
                holder.image_type.setImageResource(R.mipmap.zichanbao);
                break;
            default:
                break;
        }
        holder.text_type.setText(list.get(position).getTitle());
        holder.text_time.setText(list.get(position).getCreated_at().substring(0, 10));
        holder.des.setText(list.get(position).getContent());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetailsDailyActivity(position);
            }
        });

        return convertView;
    }

    private void goDetailsDailyActivity(int position) {
        Intent intent = new Intent(context, DetailsDailyActivity.class);
        intent.putExtra("id", list.get(position).getProjectId());
        context.startActivity(intent);
    }

    static class ViewHolder {
        ImageView image_type;
        TextView text_type;
        TextView text_time;
        TextView des;
        TextView time_title;
        TextView text_share_today ;
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
