package com.ziyawang.ziyadaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.activity.MyPublishListActivity;
import com.ziyawang.ziyadaily.entity.InfoNoticeEntity;
import com.ziyawang.ziyadaily.entity.MyPublishEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2017/6/16.
 */

public class MyPublishAdapter extends BaseAdapter {
    private Context context;
    private List<MyPublishEntity> list;

    public MyPublishAdapter() {
    }

    public MyPublishAdapter(Context context, List<MyPublishEntity> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.my_publish_items, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.des = (TextView) convertView.findViewById(R.id.des);
            holder.image_type = (ImageView) convertView.findViewById(R.id.image_type);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (list.get(position).getType()){
            case "1" :
                holder.image_type.setImageResource(R.mipmap.chushou01);
                holder.title.setText(R.string.SaleAsset);
                break;
            case "2" :
                holder.image_type.setImageResource(R.mipmap.qiugou);
                holder.title.setText(R.string.BuyRequired);
                break;
            default:
                break;
        }
        holder.des.setText(list.get(position).getCreated_at());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , MyPublishListActivity.class ) ;
                intent.putExtra("type" , list.get(position).getType() ) ;
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView title ;
        TextView des ;
        ImageView image_type ;
    }

    public void addAll(Collection<? extends MyPublishEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
