package com.example.tzadmin.tzsk_windows.SendDataModule.AbstractSendData;

import android.content.Context;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.helper;

/**
 * Created by tzadmin on 04.05.17.
 */

public abstract class SendData {

    protected SendData(Context context) {
        if(!helper.InetHasConnection(context))
            return;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Database.SetUp(dbHelper.getReadableDatabase());
    }

    public abstract void ObserverData (Object object);
}
