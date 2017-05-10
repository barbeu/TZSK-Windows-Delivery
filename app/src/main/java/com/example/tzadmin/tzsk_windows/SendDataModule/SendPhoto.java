package com.example.tzadmin.tzsk_windows.SendDataModule;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import com.example.tzadmin.tzsk_windows.AbstractSendData.SendData;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Photo;
import com.example.tzadmin.tzsk_windows.JsonModule.JSON;
import com.example.tzadmin.tzsk_windows.helper;
import com.github.kevinsawicki.http.HttpRequest;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by tzadmin on 04.05.17.
 */

public class SendPhoto extends SendData {

    public SendPhoto(Context context) {
        if (!helper.InetHasConnection(context))
            return;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Database.SetUp(dbHelper.getReadableDatabase());

        ObserverData(Database.selectPhoto(Auth.id));
    }

    @Override
    public void ObserverData(Object objects) {
        ArrayList<Photo> photos = (ArrayList<Photo>) objects;

        if (photos == null)
            return;

        new SendUnrequitedPhoto().execute(photos);
    }

    class SendUnrequitedPhoto extends AsyncTask<ArrayList<Photo>, Void, Integer> {

        @Override
        protected Integer doInBackground(ArrayList<Photo>... params) {
            ArrayList<Photo> photos = params[0];
            HttpRequest request;
            try {
                request = HttpRequest.post(helper.httpServer + helper.HTTP_QUERY_SEND_PHOTO);
                request.basic(Auth.login, Auth.passwd);
                request.part("body", JSON.generateKeysPhoto(photos));
                for (int i = 0; i < photos.size(); i++) {
                    request.part(
                            photos.get(i).DocID + "&" + photos.get(i).SerialNumber,
                            new File(photos.get(i).path));
                }
            }
            catch (Exception ex) {
                return -1;
            }
            //request.connectTimeout(5000);
            return request.code();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == helper.CODE_RESP_SERVER_OK) {
                for (File myFile : new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Delivery").listFiles())
                    if (myFile.isFile())
                        myFile.delete();
                Database.deletePhoto(Auth.id);
            }
        }
    }
}
