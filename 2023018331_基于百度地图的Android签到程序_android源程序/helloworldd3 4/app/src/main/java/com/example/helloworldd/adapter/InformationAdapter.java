package com.example.helloworldd.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.helloworldd.R;

import java.util.List;
import java.util.Map;

public class InformationAdapter extends BaseAdapter {
    // 要显示的数据的集合
    private List<Map<String, Object>> data;
    // 接受上下文
    private Context context;
    // 声明内部类对象
    private ViewHolder viewHolder;

    public InformationAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
    }


    public void setData(List<Map<String, Object>> data) {
        this.data=data;
        notifyDataSetChanged();
    }

    // 返回的总个数
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    // 返回每个条目对应的数据
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    // 返回的id
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // 返回这个条目对应的控件对象
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 判断当前条目是否为null
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.weixin_item, null);

            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.title);
            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.content);
            viewHolder.time = (TextView) convertView
                    .findViewById(R.id.time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 获取List集合中的map对象
        Map<String, Object> map = data.get(position);

        String title = map.get("title").toString();
        viewHolder.title.setText(title);
        String content;
        if(map.get("content")!=null){
            content= map.get("content").toString();
        }
        else{
            content = map.get("").toString();
        }

        viewHolder.content.setText(content);
        String time = map.get("time").toString();
        viewHolder.time.setText(time);

        return convertView;
    }

    /**
     * 内部类 记录单个条目中所有属性
     *
     *
     *
     */
    class ViewHolder {
        public TextView title;
        public TextView content;
        public TextView time;
    }
}
