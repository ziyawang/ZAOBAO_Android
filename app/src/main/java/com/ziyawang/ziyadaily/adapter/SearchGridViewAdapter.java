package com.ziyawang.ziyadaily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ziyawang.ziyadaily.R;
import com.ziyawang.ziyadaily.entity.SearchGridViewEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2017/8/2.
 */

public class SearchGridViewAdapter extends BaseAdapter{
    private Context context;
    private List<SearchGridViewEntity> list;

    public SearchGridViewAdapter() {
    }

    public SearchGridViewAdapter(Context context, List<SearchGridViewEntity> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.search_gridview_item, parent, false);
            holder = new ViewHolder();
            holder.text_title = (TextView) convertView.findViewById(R.id.text_title);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text_title.setText(list.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        TextView text_title ;
    }

    public void addAll(Collection<? extends SearchGridViewEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
