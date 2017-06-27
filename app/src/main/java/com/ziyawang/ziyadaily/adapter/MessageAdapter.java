package com.ziyawang.ziyadaily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.activity.LoginActivity;
import com.ziyawang.ziyadaily.entity.MessageEntity;
import com.ziyawang.ziyadaily.entity.MyPublishListEntity;
import com.ziyawang.ziyadaily.tools.LoadImageAsyncTask;
import com.ziyawang.ziyadaily.tools.SDUtil;
import com.ziyawang.ziyadaily.tools.ToastUtils;
import com.ziyawang.ziyadaily.tools.Url;
import com.ziyawang.ziyadaily.views.MyIconImageView;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2017/6/21.
 */

public class MessageAdapter extends BaseAdapter{

    private Context context;
    private List<MessageEntity> list;

    public MessageAdapter() {
    }

    public MessageAdapter(Context context, List<MessageEntity> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.message_list_items, parent, false);
            holder = new ViewHolder();
            holder.image = (MyIconImageView) convertView.findViewById(R.id.image);
            holder.nickName = (TextView) convertView.findViewById(R.id.nickName);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.text_message = (TextView) convertView.findViewById(R.id.text_message);
            holder.linear_reply = (LinearLayout) convertView.findViewById(R.id.linear_reply);
            holder.text_reply = (TextView) convertView.findViewById(R.id.text_reply);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        new LoadImageAsyncTask(new LoadImageAsyncTask.CallBack() {
            @Override
            public void setData(final Bitmap bitmap) {
                holder.image.setImageBitmap(bitmap);
            }
        }).execute(Url.FileIP + list.get(position).getPicture());

        if (!list.get(position).getPhone().matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
            holder.nickName.setText(list.get(position).getPhone());
        }else {
            String phoneNumber = list.get(position).getPhone() ;
            String substring = phoneNumber.substring(0, 3);
            String substring1 = phoneNumber.substring(7, 11);
            holder.nickName.setText(substring + "****" + substring1);
        }
        holder.time.setText(list.get(position).getCreated_at());
        holder.text_message.setText(list.get(position).getContent() );
        if ("".equals(list.get(position).getReply()) || TextUtils.isEmpty(list.get(position).getReply())){
            holder.linear_reply.setVisibility(View.GONE);
        }else {
            holder.linear_reply.setVisibility(View.VISIBLE);
            holder.text_reply.setText(list.get(position).getReply());
        }

        return convertView;
    }

    static class ViewHolder {
        MyIconImageView image ;
        TextView nickName ;
        TextView time ;
        TextView text_message ;
        LinearLayout linear_reply ;
        TextView text_reply ;
    }

    public void addAll(Collection<? extends MessageEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
