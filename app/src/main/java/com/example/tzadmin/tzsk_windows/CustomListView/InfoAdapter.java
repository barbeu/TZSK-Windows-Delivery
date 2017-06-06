package com.example.tzadmin.tzsk_windows.CustomListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.DeliveryInfo;
import com.example.tzadmin.tzsk_windows.R;
import java.util.ArrayList;

/**
 * Created by tzadmin on 06.06.17.
 */

public class InfoAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<DeliveryInfo> objects;

    public InfoAdapter(Context context, ArrayList<DeliveryInfo> objs) {
        ctx = context;
        objects = objs;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_delivery, parent, false);
        }

        DeliveryInfo delInfo = getInfo(position);

        ((TextView) view.findViewById(R.id.delivery_header)).setText(delInfo.header);
        ((TextView) view.findViewById(R.id.delivery_info)).setText(delInfo.body);

        return view;
    }

    DeliveryInfo getInfo(int position) {
        return ((DeliveryInfo) getItem(position));
    }
}


