package com.example.helloworldd.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.helloworldd.R;
import com.example.helloworldd.pojo.SignItem;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class SignListAdapter2 extends BaseAdapter {
    private Context context;
    public List<SignItem> items;

    public SignListAdapter2(Context context, List<SignItem> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_item, parent, false);

            holder = new ViewHolder();
            holder.tvSignCount = view.findViewById(R.id.tv_sign_count);
            holder.tvSignTime = view.findViewById(R.id.tv_sign_time);
            holder.tvTimeText =view.findViewById(R.id.tvTimeText);
            System.out.println(items.get(0).getType()+"qq");
            if (items.get(0).getType().equals("DAILY")){
                holder.tvTimeText.setVisibility(View.GONE);
            }

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        SignItem item = items.get(position);

        holder.tvSignCount.setText("第" + item.getSignCount() + "次签到");

        if (item.getSignTime() != null) {
            holder.tvSignTime.setText(item.getSignTime());
        } else {
            holder.tvSignTime.setText("");
        }


        holder.tvSignCount.setOnClickListener(v -> {
            showTimePickerDialog(item, position);
        });
        holder.tvTimeText.setOnClickListener(view1 -> {

            items.get(position).setType(holder.tvTimeText.getText().toString());
            System.out.println(holder.tvTimeText.getText().toString());
        });

        return view;
    }

    static class ViewHolder {
        Button tvSignCount;
        TextView tvSignTime;
        EditText tvTimeText;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
        private void showTimePickerDialog(SignItem item, int position) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                    (view, hourOfDay, minuteOfDay) -> {
                        LocalTime signTime = LocalTime.of(hourOfDay, minuteOfDay);
                        item.setSignTime(signTime.toString());
                        notifyDataSetChanged();
                    },
                    hour, minute, true);

            timePickerDialog.show();
        }
    }
