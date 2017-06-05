package com.example.tzadmin.tzsk_windows.DeleteModule;

import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.helper;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tzadmin on 03.06.17.
 */

public class Delete {

    int dayBackUp = -8;

    public Delete() {
        ArrayList<String> DocIDs = Database.selectDeliveriesByDel(Auth.id, dateInActive());
        deleteInactiveSwitches(DocIDs);
        deleteInactiveStatusParams(DocIDs);
        deleteInactiveDelivery();
    }

    private String dateInActive () {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, dayBackUp);
        return helper.Date(now.getTime());
    }

    private void deleteInactiveDelivery () {
        Database.deleteDeliveries(Auth.id, dateInActive());
    }

    private void deleteInactiveSwitches (ArrayList<String> ids) {
        if(ids != null) {
            for (String id : ids) {
                Database.deleteSwitches(Auth.id, id);
            }
        }
    }

    private void deleteInactiveStatusParams (ArrayList<String> ids) {
        if(ids != null) {
            for (String id : ids) {
                Database.deleteStatusParam(Auth.id, id);
            }
        }
    }
}
