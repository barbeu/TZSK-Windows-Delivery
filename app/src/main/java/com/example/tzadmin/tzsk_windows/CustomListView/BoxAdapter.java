package com.example.tzadmin.tzsk_windows.CustomListView;

/**
 * Created by tzadmin on 18.04.17.
 */

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.R;

public class BoxAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Delivery> objects;

    public BoxAdapter(Context context,ArrayList<Delivery> deliveries) {
        ctx = context;
        objects = deliveries;
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
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Delivery p = getProduct(position);

        ((TextView) view.findViewById(R.id.delivery_client)).setText(p.SerialNumber + " - " + p.Client);
        ((TextView) view.findViewById(R.id.delivery_date)).setText(p.DeliveryDate);
        return view;
    }

    Delivery getProduct(int position) {
        return ((Delivery) getItem(position));
    }
}
