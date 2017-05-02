package com.example.tzadmin.tzsk_windows.SendDataModule;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.ChangedData;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Photo;
import com.example.tzadmin.tzsk_windows.JsonModule.JSON;
import com.example.tzadmin.tzsk_windows.helper;
import com.github.kevinsawicki.http.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by tzadmin on 26.04.17.
 */

public class SendData extends AppCompatActivity {

    public SendData(Context context) {
        if(!helper.InetHasConnection(context))
            return;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Database.SetUp(dbHelper.getReadableDatabase());

        ObserverDataChanged(Database.selectDataChanged(Auth.id));
    }

    public void ObserverDataChanged (ArrayList<ChangedData> dataChanged) {
        if(dataChanged == null)
            return;

        new SendDataChanged().execute(
                JSON.generateChangedData(dataChanged));
    }

    class SendDataChanged  extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer response = HttpRequest.post(helper.httpServer + helper.HTTP_QUERY_CHANGE_DATA)
                    .basic(Auth.login, Auth.passwd)
                    .send(params[0])
                    .code();
            return response;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(result == helper.CODE_RESP_SERVER_OK) {
                Database.deleteDataChanged(Auth.id);
            }
        }
    }
}
