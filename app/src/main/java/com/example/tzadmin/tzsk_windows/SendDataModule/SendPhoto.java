package com.example.tzadmin.tzsk_windows.SendDataModule;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import com.example.tzadmin.tzsk_windows.AbstractSendData.SendData;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Photo;
import com.example.tzadmin.tzsk_windows.helper;
import com.github.kevinsawicki.http.HttpRequest;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by tzadmin on 04.05.17.
 */

public class SendPhoto extends SendData {

    SendPhoto(Context context) {
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

    class SendUnrequitedPhoto extends AsyncTask<ArrayList<Photo>, Void, HttpRequest> {

        @Override
        protected HttpRequest doInBackground(ArrayList<Photo>... params) {
            ArrayList<Photo> photos = params[0];

            HttpRequest request = HttpRequest.post(helper.httpServer + helper.HTTP_QUERY_SEND_PHOTO);
            request.basic(Auth.login, Auth.passwd);
            request.part("status[body]", "Making a multipart request");
            for (int i = 0; i < photos.size(); i++) {
                request.part("status[image]", new File(photos.get(i).path));
            }

            return request;
        }

        @Override
        protected void onPostExecute(HttpRequest result) {
            super.onPostExecute(result);
            if (result.ok()) {
                for (File myFile : new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Delivery").listFiles())
                    if (myFile.isFile())
                        myFile.delete();
                Database.deletePhoto(Auth.id);
            }
        }
    }
}
