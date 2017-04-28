package com.example.tzadmin.tzsk_windows.ChangedData;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Status;
import com.example.tzadmin.tzsk_windows.HttpModels.HttpResp;
import com.example.tzadmin.tzsk_windows.JsonModule.JSON;
import com.example.tzadmin.tzsk_windows.helper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by tzadmin on 26.04.17.
 */

public class ChangedData extends AppCompatActivity {

    public ChangedData(Context context) {
        if(!helper.InetHasConnection(context))
            return;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Database.SetUp(dbHelper.getReadableDatabase());
        ObserverStatusChanged(Database.selectStatusChanged(Auth.id));
    }

    public void ObserverStatusChanged (ArrayList<Status> statuses) {
        if(statuses == null)
            return;

        new SendStatusChanged().execute(
                helper.HTTP_QUERY_CHANGE_STATUS,
                Auth.login,
                Auth.passwd,
                JSON.generateChangedData(statuses));
    }

    class SendStatusChanged  extends AsyncTask<String, Void, HttpResp> {

        @Override
        protected HttpResp doInBackground(String... params) {
            URL url;
            HttpURLConnection connection;
            HttpResp result = new HttpResp();
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
                result.Code = resp.Code;
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(HttpResp result) {
            super.onPostExecute(result);
            if(result.Code == helper.CODE_RESP_SERVER_OK) {
                Database.deleteStatuses(Auth.id);
            }
        }
    }
}
