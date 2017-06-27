package com.ziyawang.ziyadaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.activity.DetailsDailyActivity;
import com.ziyawang.ziyadaily.entity.InfoNoticeEntity;
import com.ziyawang.ziyadaily.entity.MyMessageEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2017/6/16.
 */

public class MyMessageAdapter extends BaseAdapter {
    private Context context;
    private List<MyMessageEntity> list;

    public MyMessageAdapter() {
    }

    public MyMessageAdapter(Context context, List<MyMessageEntity> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.my_message_items, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(list.get(position).getTitle());
        if (TextUtils.isEmpty(list.get(position).getReply())){
            holder.content.setText(list.get(position).getContent());
        }else {
            holder.content.setText(list.get(position).getReply());
        }

        holder.time.setText(list.get(position).getUpdated_at());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailsDailyActivity.class ) ;
                intent.putExtra("id" , list.get(position).getProjectId() ) ;
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView title ;
        TextView content ;
        TextView time ;
    }

    public void addAll(Collection<? extends MyMessageEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
