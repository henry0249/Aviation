package com.example.administrator.aviation.model.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.administrator.aviation.R;

import java.util.List;

/**
 * Created by Administrator on 2018/10/24.
 */

public class PopWindowsAdapter extends ArrayAdapter<String> {
    private int resourceID;

    public PopWindowsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String a = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
        TextView tx = (TextView) view.findViewById(R.id.pop_listitem);
        tx.setText(a);
        return view;
    }
}
