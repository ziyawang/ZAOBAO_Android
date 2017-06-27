package com.ziyawang.ziyadaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wx.goodview.GoodView;
import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.activity.DetailsDailyActivity;
import com.ziyawang.ziyadaily.entity.HomeEntity;
import com.ziyawang.ziyadaily.entity.InfoNoticeEntity;
import com.ziyawang.ziyadaily.views.JustifyTextView;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2017/6/16.
 */

public class InfoNoticeAdapter extends BaseAdapter {
    private Context context;
    private List<InfoNoticeEntity> list;

    public InfoNoticeAdapter() {
    }

    public InfoNoticeAdapter(Context context, List<InfoNoticeEntity> list) {
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

       ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.info_notice_items, parent, false);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.des = (TextView) convertView.findViewById(R.id.des);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.time.setText(list.get(position).getUpdated_at());
        holder.des.setText(list.get(position).getContent());
        return convertView;
    }

    static class ViewHolder {
        TextView des ;
        TextView time ;
    }

    public void addAll(Collection<? extends InfoNoticeEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
