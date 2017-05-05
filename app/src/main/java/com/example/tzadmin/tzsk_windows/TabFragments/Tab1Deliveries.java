package com.example.tzadmin.tzsk_windows.TabFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.tzadmin.tzsk_windows.HttpModels.HttpResp;
import com.example.tzadmin.tzsk_windows.JsonModule.JSON;
import com.example.tzadmin.tzsk_windows.R;
import com.example.tzadmin.tzsk_windows.SendDataModule.SendChangedData;
import com.example.tzadmin.tzsk_windows.helper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by tzadmin on 17.04.17.
 */

public class Tab1Deliveries extends Fragment implements AdapterView.OnItemClickListener {
    ArrayList<Delivery> deliveries;
    BoxAdapter boxAdapter;
    ListView lvMain;
    View rootView;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab1deliveries, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_tab1deliv);
        lvMain = (ListView) rootView.findViewById(R.id.lvMain);
        lvMain.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onStart () {
        super.onStart();
        reloadDeliveries();
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

        new downloadDelivery().execute(
                helper.HTTP_QUERY_GETORDERS,
                Auth.login,
                Auth.passwd,
                JSON.generateClients(Database.selectDeliveries(Auth.id)));
    }

    private void refreshDeliveries (String jsonStringDeliveries) {
        deliveries = JSON.parse(jsonStringDeliveries);
        TextView tv = (TextView) rootView.findViewById(R.id.tvMain);

        if (deliveries == null) {
            deliveries = Database.selectDeliveries(Auth.id);
            if (deliveries == null) {
                tv.setText(Auth.login + " на данный момент у вас нет доставок.");
                return;
            }
            tv.setText("Здравствуйте! " + Auth.login);
        } else {
            Database.insertDeliveries(deliveries, Auth.id);
            deliveries = Database.selectDeliveries(Auth.id);
            tv.setText("Здравствуйте! " + Auth.login);
        }

        boxAdapter = new BoxAdapter(rootView.getContext(), deliveries);
        lvMain.setAdapter(boxAdapter);
    }

    class downloadDelivery extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lvMain.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection connection;
            String result = null;
            try {
                url = new URL(helper.httpServer + params[helper.HTTP_PARAM_QUERY]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((params[helper.HTTP_PARAM_LOGIN] + ":" + params[helper.HTTP_PARAM_PASSWORD]).getBytes(), Base64.NO_WRAP ));
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write( params[helper.HTTP_PARAM_POST_DATA] == null ? "" : params[helper.HTTP_PARAM_POST_DATA]);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();
                HttpResp resp = new HttpResp();
                resp.Code = connection.getResponseCode();
                if(resp.Code == helper.CODE_RESP_SERVER_OK)
                    result = helper.streamToString(connection.getInputStream());
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
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

