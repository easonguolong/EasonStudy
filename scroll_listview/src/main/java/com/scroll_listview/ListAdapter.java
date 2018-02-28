package com.scroll_listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2017/7/17.
 */

public class ListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> lists;

    public ListAdapter(Context context,ArrayList<String> lists){
        this.context= context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public String getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item,parent,false);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.itemdata);
            convertView.setTag(viewHolder);
        }else{
           viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(lists.get(position));
        return convertView;
    }


    class ViewHolder {
        TextView textView;
    }

}
