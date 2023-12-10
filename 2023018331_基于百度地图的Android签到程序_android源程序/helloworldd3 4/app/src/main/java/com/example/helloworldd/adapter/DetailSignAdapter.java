package com.example.helloworldd.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.helloworldd.R;

import java.util.List;
import java.util.Map;

public class DetailSignAdapter extends BaseAdapter {

    public DetailSignAdapter(List<Map<String, Object>> data, Context context) {
        this.data = data;
        this.context = context;
    }

    private List<Map<String, Object>> data;
    // 接受上下文
    private Context context;
    // 声明内部类对象
    private DetailSignAdapter.ViewHolder viewHolder;

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
            viewHolder = new DetailSignAdapter.ViewHolder();
            view = View.inflate(context, R.layout.detail_sign_item, null);
            viewHolder.signer=view.findViewById(R.id.signer);
            viewHolder.signsu=view.findViewById(R.id.signsu);
            viewHolder.signtimeee=view.findViewById(R.id.signtimeee);
            view.setTag(viewHolder);
        } else {viewHolder = (DetailSignAdapter.ViewHolder) view.getTag();}
        Map<String, Object> map = data.get(i);
        System.out.println("适配器打印"+map);

        viewHolder.signer.setText(map.get("signer").toString());
        viewHolder.signsu.setText(map.get("signsu").toString());
        String signtimeee=map.get("signtimeee").toString();
        System.out.println("-a"+signtimeee);
        if (signtimeee.equals("2022-12-10 00:00:00.0"))
        {
            viewHolder.signtimeee.setText("");

        }else {
            viewHolder.signtimeee.setText(signtimeee);
        }
        return view;
    }

    class ViewHolder {
        public TextView signer;
        public TextView signsu;
        public TextView signtimeee;

    }
}
