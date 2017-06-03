package com.example.tzadmin.tzsk_windows.DeleteModule;

import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.helper;
import java.util.Calendar;

/**
 * Created by tzadmin on 03.06.17.
 */

public class Delete {

    int dayBackUp = -8;

    public Delete() {
        deleteInactiveDelivery();
    }

    private void deleteInactiveDelivery () {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, dayBackUp);
        Database.deleteDeliveries(Auth.id, helper.Date(now.getTime()));
    }
}
