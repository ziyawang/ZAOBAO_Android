package com.ziyawang.ziyadaily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.entity.MyPublishListEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2017/6/16.
 */

public class MyPublishListAdapter extends BaseAdapter {
    private Context context;
    private List<MyPublishListEntity> list;

    public MyPublishListAdapter() {}

    public MyPublishListAdapter(Context context, List<MyPublishListEntity> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.my_publish_list_items, parent, false);
            holder = new ViewHolder();
            holder.text_des = (TextView) convertView.findViewById(R.id.text_des);
            holder.text_ben = (TextView) convertView.findViewById(R.id.text_ben);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text_des.setText(list.get(position).getDescribe());
        holder.text_ben.setText(list.get(position).getLabel());
        holder.time.setText(list.get(position).getCreated_at().substring(0 , 16));
        return convertView;
    }

    static class ViewHolder {
        TextView text_des ;
        TextView text_ben ;
        TextView time ;
    }

    public void addAll(Collection<? extends MyPublishListEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
