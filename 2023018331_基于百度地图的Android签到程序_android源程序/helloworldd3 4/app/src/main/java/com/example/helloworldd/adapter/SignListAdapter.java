package com.example.helloworldd.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.helloworldd.R;

import java.util.List;
import java.util.Map;

public class SignListAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    // 接受上下文
    private Context context;
    // 声明内部类对象
    private ViewHolder viewHolder;

    public SignListAdapter(List<Map<String, Object>> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.sign_item, null);
            viewHolder.SIGNNAME = (TextView) view.findViewById(R.id.SIGNNAME);
            viewHolder.SIGNTIME=view.findViewById(R.id.SIGNTIME);
            viewHolder.SIGNLIMIT=view.findViewById(R.id.SIGNLIMIT);
            viewHolder.SIGNNUM=view.findViewById(R.id.SIGNNUM);
            view.setTag(viewHolder);
        } else {viewHolder = (ViewHolder) view.getTag();}
        Map<String, Object> map = data.get(i);
        System.out.println(map);

        String SIGNNAME1 = map.get("SIGNNAME").toString();
        viewHolder.SIGNNAME.setText(SIGNNAME1);
        String SIGNTIME1 = map.get("SIGNTIME").toString();
        viewHolder.SIGNTIME.setText(SIGNTIME1);
        String SIGNLIMIT1 = map.get("SIGNLIMIT").toString();
        viewHolder.SIGNLIMIT.setText(SIGNLIMIT1);
        String SIGNNUM1 = "签到人数  "+map.get("SIGNNUM").toString();
        System.out.println(SIGNTIME1);
        viewHolder.SIGNNUM.setText(SIGNNUM1);
        return view;
    }
    class ViewHolder {
        public TextView SIGNNAME;
        public TextView SIGNTIME;
        public TextView SIGNLIMIT;
        public TextView SIGNNUM;
    }
}
