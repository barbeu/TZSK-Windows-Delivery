package com.example.tzadmin.tzsk_windows.TabFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.CustomListView.BoxAdapter;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.DeliveriesActivity;
import com.example.tzadmin.tzsk_windows.JsonModule.JSON;
import com.example.tzadmin.tzsk_windows.R;
import com.example.tzadmin.tzsk_windows.helper;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

/**
 * Created by tzadmin on 17.04.17.
 */

public class Tab1Deliveries extends Fragment implements AdapterView.OnItemClickListener {
    ArrayList<Delivery> deliveries;
    BoxAdapter boxAdapter;
    ListView lvMain;
    View rootView;
    ProgressBar progressBar;
    String dateDelivery = null;
    HorizontalCalendar horizontalCalendar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab1deliveries, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_tab1deliv);
        lvMain = (ListView) rootView.findViewById(R.id.lvMain);
        lvMain.setOnItemClickListener(this);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.WEEK_OF_MONTH, 2);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.WEEK_OF_MONTH, -1);

        horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                dateDelivery = helper.Date(date);
                reloadDeliveries();
            }
        });

        return rootView;
    }

    @Override
    public void onStart () {
        super.onStart();
        Date date = new Date();
        horizontalCalendar.selectDate(date, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Delivery delivery = (Delivery)parent.getItemAtPosition(position);
        int idDelivery = delivery.id;
        Intent intent = new Intent(getActivity(), DeliveriesActivity.class);
        intent.putExtra("id", idDelivery);
        startActivity(intent);
    }

    public void reloadDeliveries() {
        if(!helper.InetHasConnection(getActivity()))
            helper.message(getActivity(), helper.MSG.INTERNET_NOT_CONNECTING, Toast.LENGTH_LONG);

        new downloadDelivery().execute();
    }

    private void refreshDeliveries (String jsonStringDeliveries) {
        deliveries = JSON.parse(jsonStringDeliveries);
        TextView tv = (TextView) rootView.findViewById(R.id.tvMain);

        if(deliveries != null)
            Database.insertDeliveries(deliveries, Auth.id);

        deliveries = Database.selectDeliveries(Auth.id, dateDelivery);
        if (deliveries == null) {
            tv.setText(Auth.login + " на данный момент у вас нет доставок.");
            lvMain.setAdapter(null);
            return;
        }
        tv.setText("Здравствуйте! " + Auth.login);

        boxAdapter = new BoxAdapter(rootView.getContext(), deliveries);
        lvMain.setAdapter(boxAdapter);
    }

    class downloadDelivery extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lvMain.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {

            String JsonStringRequest =
                    JSON.generateClients(
                            Database.selectDeliveries(Auth.id, dateDelivery), dateDelivery);

            String result = null;
            try {
                result = helper.streamToString(
                        HttpRequest.post(helper.httpServer + helper.HTTP_QUERY_GETORDERS)
                        .basic(Auth.login, Auth.passwd)
                        .send(JsonStringRequest)
                        .stream()
                );
            } catch (Exception e) {
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            refreshDeliveries(result);
            progressBar.setVisibility(View.INVISIBLE);
            lvMain.setVisibility(View.VISIBLE);
        }
    }
}

