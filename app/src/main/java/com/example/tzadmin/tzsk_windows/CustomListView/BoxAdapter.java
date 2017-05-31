package com.example.tzadmin.tzsk_windows.CustomListView;

/**
 * Created by tzadmin on 18.04.17.
 */

import java.util.ArrayList;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.R;
import com.example.tzadmin.tzsk_windows.helper;

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

        ((TextView) view.findViewById(R.id.delivery_client)).setText(p.Client);

        int mipmap = 0;
        switch (p.Status) {
            case helper.INDEX_STATUS_NEW:
                mipmap = R.drawable.ic_delivery_status_new;
                break;
            case helper.INDEX_STATUS_PROCESS:
                mipmap = R.drawable.ic_delivery_status_work;
                break;
            case helper.INDEX_STATUS_COMPLETED:
                mipmap = R.drawable.ic_delivery_status_completed;
                break;
            case helper.INDEX_STATUS_CANCEL:
                mipmap = R.drawable.ic_delivery_status_cancel;
                break;
            case helper.INDEX_STATUS_WAITING:
                mipmap = R.drawable.ic_delivery_status_pause;
                break;
        }

        ((ImageView) view.findViewById(R.id.delivery_status_image)).setImageResource(mipmap);
        ((TextView) view.findViewById(R.id.delivery_mileage)).setText(p.Mileage + " км.");

        return view;
    }

    Delivery getProduct(int position) {
        return ((Delivery) getItem(position));
    }
}
