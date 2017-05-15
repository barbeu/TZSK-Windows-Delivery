package com.example.tzadmin.tzsk_windows.SendDataModule;

import android.content.Context;
import android.os.AsyncTask;
import com.example.tzadmin.tzsk_windows.SendDataModule.AbstractSendData.SendData;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.ChangedData;
import com.example.tzadmin.tzsk_windows.JsonModule.JSON;
import com.example.tzadmin.tzsk_windows.helper;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.ArrayList;

/**
 * Created by tzadmin on 26.04.17.
 */

public class NoGlobalData extends SendData {

    public NoGlobalData(Context context) {
        super(context);
        ObserverData(
                Database.selectDataChanged(Auth.id, 0)
        );
    }

    @Override
    public void ObserverData(Object object) {
        ArrayList<ChangedData> dataChanged = (ArrayList<ChangedData>) object;

        if(dataChanged == null)
            return;

        new SendDataChanged().execute(
                JSON.generateNoGlobalStatus(dataChanged));
    }

    class SendDataChanged extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer response = null;
            try {
                response = HttpRequest.post(helper.httpServer + helper.HTTP_QUERY_CHANGE_DATA)
                        .basic(Auth.login, Auth.passwd)
                        .send(params[0])
                        .code();
            } catch (Exception ex) {
                return -1;
            }

            return response;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(result == helper.CODE_RESP_SERVER_OK) {
                Database.deleteDataChanged(Auth.id, 0);
            }
        }
    }
}
