package com.example.helloworldd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.helloworldd.R;
import com.example.helloworldd.pojo.SignRecord;

import java.util.List;

public class SignRecordAdapter extends BaseAdapter {
    private List<SignRecord> signRecords;
    private LayoutInflater inflater;

    public SignRecordAdapter(Context context, List<SignRecord> signRecords) {
        this.signRecords = signRecords;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return signRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return signRecords.get(position);
    }

    @Override
    public long  getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_sign_record, null);
            holder = new ViewHolder();
            holder.textViewText = convertView.findViewById(R.id.text);
            holder.textViewSignSuccess = convertView.findViewById(R.id.sign_success);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SignRecord signRecord = signRecords.get(position);
        holder.textViewText.setText(signRecord.getText());
        holder.textViewSignSuccess.setText(signRecord.getSignsuccess());

        return convertView;
    }

    static class ViewHolder {
        TextView textViewText;
        TextView textViewSignSuccess;
    }
}