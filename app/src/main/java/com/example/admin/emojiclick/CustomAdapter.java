package com.example.admin.emojiclick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    List<Contact> items;

    public CustomAdapter(Context context, List<Contact> items){
        this.items=items;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(items.size()!=0){
            return items.size();
        }
        return 0;
    }

    @Override
    public Contact getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.row_top_item, parent, false);
        }
        TextView textNumber= (TextView) convertView.findViewById(R.id.tv_top_number);
        TextView textName= (TextView) convertView.findViewById(R.id.tv_top_name);
        TextView textScore= (TextView) convertView.findViewById(R.id.tv_top_score);
        Contact current=getItem(position);
        textNumber.setText(""+current.getNumber());
        textName.setText(current.getName());
        textScore.setText(""+current.getScore());
        return convertView;
    }
}
