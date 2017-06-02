package com.example.tzadmin.tzsk_windows.TabFragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.CustomListView.BoxAdapter;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.DeliveriesActivity;
import com.example.tzadmin.tzsk_windows.JsonModule.JSON;
import com.example.tzadmin.tzsk_windows.R;
import java.util.ArrayList;

/**
 * Created by tzadmin on 17.04.17.
 */

public class Tab1Deliveries extends Fragment implements AdapterView.OnItemClickListener {
    ArrayList<Delivery> deliveries;
    BoxAdapter boxAdapter;
    public ListView lvMain;
    View rootView;
    public ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab1deliveries, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_tab1deliv);
        lvMain = (ListView) rootView.findViewById(R.id.lvMain);
        lvMain.setOnItemClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lvMain.setNestedScrollingEnabled(true);
        }

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Delivery delivery = (Delivery)parent.getItemAtPosition(position);
        int idDelivery = delivery.id;
        Intent intent = new Intent(getActivity(), DeliveriesActivity.class);
        intent.putExtra("id", idDelivery);
        startActivityForResult(intent, 1);
    }

    public void refreshDeliveries (String jsonStringDeliveries, String dateDelivery) {
        deliveries = JSON.parseDeliveries(jsonStringDeliveries);
        TextView tv = (TextView) rootView.findViewById(R.id.tvMain);

        if(deliveries != null) {
            Database.insertDeliveries(deliveries, Auth.id);
        }

        deliveries = Database.selectDeliveries(Auth.id, dateDelivery);
        if (deliveries == null) {
            tv.setText(Auth.login + " на этот день у вас пока нет доставок.");
            lvMain.setAdapter(null);
            return;
        }
        tv.setText("Здравствуйте! " + Auth.login);

        boxAdapter = new BoxAdapter(rootView.getContext(), deliveries);
        lvMain.setAdapter(boxAdapter);
    }
}

