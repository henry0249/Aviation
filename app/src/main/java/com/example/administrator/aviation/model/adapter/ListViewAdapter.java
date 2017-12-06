package com.example.administrator.aviation.model.adapter;

/**
 * Created by Administrator on 2017/12/6.
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.aviation.R;

/**
 * 适配器
 * @author Administrator
 *
 */
public class ListViewAdapter extends BaseAdapter{

    private LayoutInflater inflater;

    private ArrayList<String> list;



    public ListViewAdapter(Context context, ArrayList<String> list) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lv_items_gnculd, null);
        }
        TextView tv = (TextView)convertView.findViewById(R.id.text3);
        tv.setText(list.get(position));

        return convertView;
    }

}
