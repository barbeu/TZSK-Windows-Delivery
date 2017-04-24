package com.example.tzadmin.tzsk_windows.HttpModule;

import android.os.AsyncTask;
import android.util.Base64;
import com.example.tzadmin.tzsk_windows.helper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by tzadmin on 07.04.17.
 */

public class Http {
    HttpResp responce = null;

    public HttpResp GET (String query, String login, String password) {
        getTask task = new getTask();
        task.execute(query, login, password);
        try {
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpResp POST (String query, String login, String password, String postData) {
        postTask task = new postTask();
        task.execute(query, login, password, postData);
        try {
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    class getTask extends AsyncTask<String, Void, HttpResp> {
        @Override
        protected HttpResp doInBackground(String... params) {
            try {
                URL url = new URL(helper.httpServer + params[helper.HTTP_PARAM_QUERY]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((params[helper.HTTP_PARAM_LOGIN] + ":" + params[helper.HTTP_PARAM_PASSWORD]).getBytes(), Base64.NO_WRAP ));
                connection.connect();
                responce = new HttpResp();
                responce.Code = connection.getResponseCode();
                responce.Message = connection.getResponseMessage();
                if(responce.Code == helper.CODE_RESP_SERVER_OK)
                    responce.body = helper.streamToString(connection.getInputStream());
                return responce;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute (HttpResp result) {
            responce = result;
        }
    }

    class postTask extends AsyncTask<String, Void, HttpResp> {
        @Override
        protected HttpResp doInBackground(String... params) {
            try {
                URL url = new URL(helper.httpServer + params[helper.HTTP_PARAM_QUERY]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((params[helper.HTTP_PARAM_LOGIN] + ":" + params[helper.HTTP_PARAM_PASSWORD]).getBytes(), Base64.NO_WRAP ));
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(params[helper.HTTP_PARAM_POST_DATA]);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();
                responce = new HttpResp();
                responce.Code = connection.getResponseCode();
                responce.Message = connection.getResponseMessage();
                if(responce.Code == helper.CODE_RESP_SERVER_OK)
                    responce.body = helper.streamToString(connection.getInputStream());
                return responce;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute (HttpResp result) {
            responce = result;
        }
    }
}