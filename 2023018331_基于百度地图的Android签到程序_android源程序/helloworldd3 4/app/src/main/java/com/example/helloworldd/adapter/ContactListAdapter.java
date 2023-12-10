package com.example.helloworldd.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.helloworldd.R;
import com.example.helloworldd.tools.UserPin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactListAdapter extends BaseAdapter {

    //自定义handler机制
    private Bitmap img;
    private ViewHolder viewHolder;
    private List<UserPin> list = new ArrayList<>();
    //private List<Integer> list2 = null;
    private List<Map<String, String>> data = new ArrayList<>();
    private Context mContext;

    public ContactListAdapter(Context mContext, List<UserPin> list) {
        this.mContext = mContext;
        this.list = list;
        //this.list2 = list2;
        this.data = data;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setData(List<UserPin> list){
        this.list=list;
        notifyDataSetChanged();
    }

    public View getView(final int position, View view, ViewGroup arg2) {

        final UserPin user = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            //获取listview对应的item布局
            view = LayoutInflater.from(mContext).inflate(R.layout.contactlist_item, null);
            //初始化组件
            //viewHolder.img = (ImageView) view.findViewById(R.id.img);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);
            viewHolder.contact_count = (TextView) view.findViewById(R.id.contact_count);
            viewHolder.divider = (View) view.findViewById(R.id.divider);
            view.setTag(viewHolder);
        } else {
            System.out.println("position=" + position);
            viewHolder = (ViewHolder) view.getTag();
        }
        //0~3是标签单独处理
        if (position == 0 || position == 1 || position == 2 || position == 3) {
            viewHolder.catalog.setVisibility(View.GONE);
            //viewHolder.img.setImageResource(list2.get(position));
            viewHolder.name.setText(this.list.get(position).getName());
            viewHolder.divider.setVisibility(View.GONE);
            viewHolder.contact_count.setVisibility(View.GONE);
        } else {
            //根据position获取首字母作为目录catalog
            String catalog = list.get(position).getName().charAt(0)+"";
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(catalog)) {
                viewHolder.catalog.setVisibility(View.VISIBLE);
                viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
            } else {
                viewHolder.catalog.setVisibility(View.GONE);
            }
//            Map<String, String> map = data.get(0);

            viewHolder.name.setText(this.list.get(position).getName());
            if (position == list.size() - 1) {
                viewHolder.divider.setVisibility(View.VISIBLE);
                viewHolder.contact_count.setVisibility(View.VISIBLE);
                viewHolder.contact_count.setText(Integer.toString(position +1) + "个朋友");
            } else {
                viewHolder.divider.setVisibility(View.GONE);
                viewHolder.contact_count.setVisibility(View.GONE);
            }
        }
        return view;
    }

    final static class ViewHolder {
        TextView catalog;
        TextView name;
        TextView contact_count;
        View divider;
    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            if (list.get(i).getName() != "新的朋友" && list.get(i).getName() != "群聊" &&
                    list.get(i).getName() != "标签" && list.get(i).getName() != "公众号") {
                String sortStr = list.get(i).getFirstLetter();
                if (catalog.equalsIgnoreCase(sortStr)) {
                    return i;
                }
            }
        }
        return -1;
    }

}