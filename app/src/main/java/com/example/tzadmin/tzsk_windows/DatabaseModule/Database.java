package com.example.tzadmin.tzsk_windows.DatabaseModule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Status;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.User;
import java.util.ArrayList;

/**
 * Created by tzadmin on 13.04.17.
 */

public class Database {
    private static SQLiteDatabase db;

    public static void SetUp(SQLiteDatabase dbHelper) {
        db = dbHelper;
    }

    public static void insertDeliveries (ArrayList<Delivery> deliveriesResp, int user_id) {
        for (Delivery meas : deliveriesResp) {
            ContentValues cv = new ContentValues();
            cv.put("idUser", user_id);
            cv.put("DeliveryDate", meas.DeliveryDate);
            cv.put("DocID", meas.DocID);
            cv.put("SerialNumber", meas.SerialNumber);
            cv.put("Client", meas.Client);
            cv.put("Address", meas.Address);
            cv.put("ContactDetails", meas.ContactDetails);
            cv.put("NumberOfProducts", meas.NumberOfProducts);
            cv.put("Task", meas.Task);
            cv.put("Mileage", meas.Mileage);
            cv.put("Status", meas.Status);
            db.insert("tbDeliveries", null, cv);
        }
    }

    public static void updateDelivery (Delivery delivery) {
        ContentValues cv = new ContentValues();
        cv.put("Summ", delivery.Summ);
        cv.put("Status", delivery.Status);
        db.update("tbDeliveries", cv, "id = ? AND idUser = ?", new String[] {
                String.valueOf(delivery.id),
                String.valueOf(delivery.idUser)
        });
    }

    public static void deleteDeliveries() {
        db.delete("tbDeliveries", null, null);
    }

    public static Delivery selectDelivery (int id, int user_id) {
        Cursor cursor = db.query("tbDeliveries", null, "id=" + id + " AND idUser=" + user_id, null, null, null, null, null);
        Delivery delivery = new Delivery();
        if(cursor.moveToFirst()) {
            delivery.id = cursor.getInt(0);
            delivery.idUser = cursor.getInt(1);
            delivery.DeliveryDate = cursor.getString(2);
            delivery.DocID = cursor.getString(3);
            delivery.SerialNumber = cursor.getString(4);
            delivery.Client = cursor.getString(5);
            delivery.Address = cursor.getString(6);
            delivery.ContactDetails = cursor.getString(7);
            delivery.NumberOfProducts = cursor.getString(8);
            delivery.Task = cursor.getString(9);
            delivery.Mileage = cursor.getString(10);
            delivery.Status = cursor.getInt(11);
            return delivery;
        } else {
            return null;
        }

    }

    public static ArrayList<Delivery> selectDeliveries(int user_id) {

        Cursor cursor = db.query("tbDeliveries", null, "idUser = " + user_id, null, null, null, null, null);
        ArrayList<Delivery> deliveries = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Delivery delivery = new Delivery();
                delivery.id = cursor.getInt(0);
                delivery.idUser = cursor.getInt(1);
                delivery.DeliveryDate = cursor.getString(2);
                delivery.DocID = cursor.getString(3);
                delivery.SerialNumber = cursor.getString(4);
                delivery.Client = cursor.getString(5);
                delivery.Address = cursor.getString(6);
                delivery.ContactDetails = cursor.getString(7);
                delivery.NumberOfProducts = cursor.getString(8);
                delivery.Task = cursor.getString(9);
                delivery.Mileage = cursor.getString(10);
                delivery.Status = cursor.getInt(11);
                deliveries.add(delivery);
            } while (cursor.moveToNext());
            return deliveries;
        }
        else
            return null;
    }

    public static int insertUser (String login, String password) {
        ContentValues cv = new ContentValues();
        cv.put("login", login);
        cv.put("password", password);
        cv.put("autoLogin", 1);
        db.insert("tbUsers", null, cv);
        User user = lastUserLogin();
        return user.id;
    }

    public static int isUserExist (String login, String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM tbUsers WHERE login = ? AND password = ?", new String[] {login, password});
        if(cursor.getCount() == 0)
            return -1;
        else {
            cursor.moveToNext();
            return cursor.getInt(0);
        }
    }

    public static void updateUser (int id, Integer autoLogin) {
        ContentValues cv = new ContentValues();
        cv.put("autoLogin", autoLogin);
        db.update("tbUsers", cv, "id = ?", new String[] { String.valueOf(id) });
    }

    public static void deleteUser (int id) {
        db.delete("tbUsers", "id=" + id, null);
    }

    public static User lastUserLogin () {
        Cursor cursor = db.query("tbUsers", null, "autoLogin=1", null, null, null, null, "1");
        if(cursor.getCount() == 0)
            return null;
        cursor.moveToNext();
        User user = new User();
        user.id = cursor.getInt(0);
        user.login = cursor.getString(1);
        user.password = cursor.getString(2);
        user.autoLogin = cursor.getInt(3);
        return user;
    }

    public static void insertStatusChanged (Status status) {
        if(status == null)
            return;
        ContentValues cv = new ContentValues();
        cv.put("idUser", status.idUser);
        cv.put("DocID", status.DocID);
        cv.put("SerialNumber", status.SerialNumber);
        cv.put("Status", status.Status);
        cv.put("Date", status.Date);
        db.insert("tbChangedStatus", null, cv);
    }

    public static ArrayList<Status> selectStatusChanged (int user_id) {
        Cursor cursor = db.query("tbChangedStatus", null, "idUser = " + user_id, null, null, null, null, null);
        ArrayList<Status> statuses = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Status status = new Status();
                status.id = cursor.getInt(0);
                status.idUser = cursor.getInt(1);
                status.DocID = cursor.getString(2);
                status.SerialNumber = cursor.getString(3);
                status.Status = cursor.getInt(4);
                status.Date = cursor.getString(5);
                statuses.add(status);
            } while (cursor.moveToNext());
            return statuses;
        }
        else
            return null;
    }

}
