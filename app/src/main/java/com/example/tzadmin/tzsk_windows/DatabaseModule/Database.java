package com.example.tzadmin.tzsk_windows.DatabaseModule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.ChangedData;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Photo;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.StatusParam;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Switches;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.User;
import com.example.tzadmin.tzsk_windows.helper;
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
        for (Delivery delivery : deliveriesResp) {
            ContentValues cv = new ContentValues();
            cv.put("idUser", user_id);
            cv.put("DeliveryDate", delivery.DeliveryDate);
            cv.put("day", delivery.day);
            cv.put("month", delivery.month);
            cv.put("year", delivery.year);
            cv.put("DocID", delivery.DocID);
            cv.put("SerialNumber", delivery.SerialNumber);
            cv.put("Client", delivery.Client);
            cv.put("Address", delivery.Address);
            cv.put("ContactDetails", delivery.ContactDetails);
            cv.put("NumberOfProducts", delivery.NumberOfProducts);
            cv.put("Task", delivery.Task);
            cv.put("Mileage", delivery.Mileage);
            cv.put("ChangedData", delivery.Status);
            cv.put("lat", delivery.lati);
            cv.put("long", delivery.longi);
            db.insert("tbDeliveries", null, cv);
        }
    }

    public static void updateDelivery (Delivery delivery) {
        ContentValues cv = new ContentValues();
        cv.put("Summ", delivery.Summ);
        cv.put("ChangedData", delivery.Status);
        db.update("tbDeliveries", cv, "id = ? AND idUser = ?", new String[] {
                String.valueOf(delivery.id),
                String.valueOf(delivery.idUser)
        });
    }

    public static boolean isTryStatusChanged (int user_id, String date) {
        Cursor cursor = db.query("tbDeliveries", null,
                "idUser = " + user_id +
                        " AND day =" + helper.getDay(date) +
                        " AND month =" + helper.getMonth(date) +
                        " AND year =" + helper.getYear(date),
                null, null, null, null, null);
        int count_status_try = 0;
        if (cursor.moveToFirst()) {
            do {
               int status = cursor.getInt(cursor.getColumnIndex("ChangedData"));
                if(status == 1)
                    count_status_try++;
            } while (cursor.moveToNext());
        }
        if(count_status_try > 0)
            return false;
        else
            return true;
    }

    public static int selectLastSerialNumberDelivery (int user_id, String DocID) {
        Cursor cursor = db.rawQuery(
                "SELECT max(SerialNumber) FROM tbDeliveries WHERE idUser = "
                        + user_id + " AND DocID = ?", new String[] { DocID }
        );
        if(cursor.getCount() == 0)
            return -1;
        else {
            cursor.moveToNext();
            return cursor.getInt(0);
        }
    }

    public static void deleteDeliveries() {
        db.delete("tbDeliveries", null, null);
    }

    @Nullable
    public static Delivery selectDelivery (int id, int user_id) {
        Cursor cursor = db.query("tbDeliveries", null, "id=" + id + " AND idUser=" + user_id, null, null, null, null, null);
        Delivery delivery = new Delivery();
        if(cursor.moveToFirst()) {
            delivery.id = cursor.getInt(0);
            delivery.idUser = cursor.getInt(1);
            delivery.DeliveryDate = cursor.getString(2);
            delivery.day = cursor.getInt(3);
            delivery.month = cursor.getInt(4);
            delivery.year = cursor.getInt(5);
            delivery.DocID = cursor.getString(6);
            delivery.SerialNumber = cursor.getString(7);
            delivery.Client = cursor.getString(8);
            delivery.Address = cursor.getString(9);
            delivery.ContactDetails = cursor.getString(10);
            delivery.NumberOfProducts = cursor.getString(11);
            delivery.Task = cursor.getString(12);
            delivery.Mileage = cursor.getString(13);
            delivery.Status = cursor.getInt(14);
            delivery.Summ = cursor.getInt(15);
            delivery.lati = cursor.getString(16);
            delivery.longi = cursor.getString(17);
            return delivery;
        } else {
            return null;
        }

    }

    public static Delivery selectDelivery (int user_id, String DocID, int SerialNumber) {
        Cursor cursor = db.query(
                "tbDeliveries",
                null,
                "idUser=" + user_id + " AND DocID = ? AND SerialNumber =" + SerialNumber,
                new String[] { DocID },
                null, null, null, null);
        Delivery delivery = new Delivery();
        if(cursor.moveToFirst()) {
            delivery.id = cursor.getInt(0);
            delivery.idUser = cursor.getInt(1);
            delivery.DeliveryDate = cursor.getString(2);
            delivery.day = cursor.getInt(3);
            delivery.month = cursor.getInt(4);
            delivery.year = cursor.getInt(5);
            delivery.DocID = cursor.getString(6);
            delivery.SerialNumber = cursor.getString(7);
            delivery.Client = cursor.getString(8);
            delivery.Address = cursor.getString(9);
            delivery.ContactDetails = cursor.getString(10);
            delivery.NumberOfProducts = cursor.getString(11);
            delivery.Task = cursor.getString(12);
            delivery.Mileage = cursor.getString(13);
            delivery.Status = cursor.getInt(14);
            delivery.Summ = cursor.getInt(15);
            delivery.lati = cursor.getString(16);
            delivery.longi = cursor.getString(17);
            return delivery;
        } else {
            return null;
        }
    }

    @Nullable
    public static ArrayList<Delivery> selectDeliveries(int user_id, String date) {
        Cursor cursor = db.query("tbDeliveries", null,
                "idUser = " + user_id +
                        " AND day =" + helper.getDay(date) +
                        " AND month =" + helper.getMonth(date) +
                        " AND year =" + helper.getYear(date),
                null, null, null, null, null);
        ArrayList<Delivery> deliveries = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Delivery delivery = new Delivery();
                delivery.id = cursor.getInt(0);
                delivery.idUser = cursor.getInt(1);
                delivery.DeliveryDate = cursor.getString(2);
                delivery.day = cursor.getInt(3);
                delivery.month = cursor.getInt(4);
                delivery.year = cursor.getInt(5);
                delivery.DocID = cursor.getString(6);
                delivery.SerialNumber = cursor.getString(7);
                delivery.Client = cursor.getString(8);
                delivery.Address = cursor.getString(9);
                delivery.ContactDetails = cursor.getString(10);
                delivery.NumberOfProducts = cursor.getString(11);
                delivery.Task = cursor.getString(12);
                delivery.Mileage = cursor.getString(13);
                delivery.Status = cursor.getInt(14);
                delivery.Summ = cursor.getInt(15);
                delivery.lati = cursor.getString(16);
                delivery.longi = cursor.getString(17);
                deliveries.add(delivery);
            } while (cursor.moveToNext());
            return deliveries;
        }
        else
            return null;
    }

    @Nullable
    public static String selectDocIDOnDate(int user_id, String date) {
        Cursor cursor = db.query("tbDeliveries", null,
                "idUser = " + user_id +
                        " AND day =" + helper.getDay(date) +
                        " AND month =" + helper.getMonth(date) +
                        " AND year =" + helper.getYear(date),
                null, null, null, null, "1");
        if (cursor.moveToFirst()) {
            Delivery delivery = new Delivery();
            delivery.DocID = cursor.getString(
                    cursor.getColumnIndex("DocID")
            );
            return delivery.DocID;
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

    @Nullable
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

    public static void insertDataChanged (ChangedData data) {
        if(data == null)
            return;
        ContentValues cv = new ContentValues();
        cv.put("idUser", data.idUser);
        cv.put("DocID", data.DocID);
        cv.put("isGlobal", data.isGlobal);
        cv.put("SerialNumber", data.SerialNumber);
        cv.put("Status", data.Status);
        cv.put("Summ", data.summ);
        cv.put("Date", data.Date);
        db.insert("tbChangedStatus", null, cv);
    }

    public static void updateStatusDelivery(String DocID, int index) {
        ContentValues cv = new ContentValues();
        cv.put("ChangedData", 1/*В работе*/);
        db.update("tbDeliveries", cv, "DocID = ? AND SerialNumber = ?", new String[] {
                DocID,
                String.valueOf(index)
        });
    }

    @Nullable
    public static ArrayList<ChangedData> selectDataChanged (int user_id, int isGlobal) {
        Cursor cursor = db.query(
                "tbChangedStatus",
                null,
                "idUser = " + user_id + " AND isGlobal = " + (isGlobal == 0 ? 0 : 1),
                null,
                null,
                null,
                null,
                null
        );
        ArrayList<ChangedData> Data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ChangedData data = new ChangedData();
                data.id = cursor.getInt(0);
                data.idUser = cursor.getInt(1);
                data.isGlobal = cursor.getInt(2);
                data.DocID = cursor.getString(3);
                data.SerialNumber = cursor.getString(4);
                data.Status = cursor.getInt(5);
                data.summ = cursor.getInt(6);
                data.Date = cursor.getString(7);
                Data.add(data);
            } while (cursor.moveToNext());
            return Data;
        }
        else
            return null;
    }

    public static void deleteDataChanged (int user_id, int isGlobal) {
        db.delete(
                "tbChangedStatus",
                "idUser =" + user_id + " AND isGlobal =" + (isGlobal == 0 ? 0 : 1),
                null
        );
    }

    public static void insertPhoto (Photo photo) {
        if(photo == null)
            return;
        ContentValues cv = new ContentValues();
        cv.put("idUser", photo.idUser);
        cv.put("DocID", photo.DocID);
        cv.put("SerialNumber", photo.SerialNumber);
        cv.put("PathPhoto", photo.path);
        db.insert("tbPhotos", null, cv);
    }

    @Nullable
    public static ArrayList<Photo> selectPhoto (int user_id) {
        Cursor cursor = db.query("tbPhotos", null, "idUser = " + user_id, null, null, null, null, null);
        ArrayList<Photo> photos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Photo photo = new Photo();
                photo.id = cursor.getInt(0);
                photo.idUser = cursor.getInt(1);
                photo.DocID = cursor.getString(2);
                photo.SerialNumber = cursor.getString(3);
                photo.path = cursor.getString(4);
                photos.add(photo);
            } while (cursor.moveToNext());
            return photos;
        }
        else
            return null;
    }

    public static void deletePhoto (int user_id) {
        db.delete("tbPhotos",
                "idUser =" + user_id, null);
    }

    public static void insertStatusParam (StatusParam param) {
        if(param == null)
            return;
        ContentValues cv = new ContentValues();
        cv.put("idUser", param.idUser);
        cv.put("DocID", param.DocID);
        cv.put("AllMileage", param.AllMileage);
        cv.put("AllOdmtr", param.AllOdmtr);
        db.insert("tbStatusParam", null, cv);
    }

    @Nullable
    public static StatusParam selectStatusParam (int user_id, String DocID) {
        Cursor cursor = db.query("tbStatusParam",
                null,
                "idUser = " + user_id + " AND DocID = ?",
                new String[] { DocID },
                null, null, null, null);
        if (cursor.moveToFirst()) {
            StatusParam statusParam = new StatusParam();
            statusParam.id = cursor.getInt(0);
            statusParam.idUser = cursor.getInt(1);
            statusParam.DocID = cursor.getString(2);
            statusParam.AllMileage = cursor.getInt(3);
            statusParam.AllOdmtr = cursor.getInt(4);
            return statusParam;
        }
        else
            return null;
    }

    public static void deleteStatusParam (int user_id, String DocID) {
        if(DocID == null)
            return;
        db.delete(
                "tbStatusParam",
                "idUser =" + user_id + " AND DocID = ?",
                new String[] { DocID }
        );
    }

    public static void insertSwitches (Switches switches) {
        if(switches == null)
            return;
        ContentValues cv = new ContentValues();
        cv.put("idUser", switches.idUser);
        cv.put("DocID", switches.DocID);
        cv.put("getStarted", switches.getStarted);
        cv.put("finishUnloading", switches.finishUnloading);
        cv.put("finishWork", switches.finishWork);
        cv.put("valueOdmtr", switches.valueOdmtr);
        db.insert("tbSwitches", null, cv);
    }

    @Nullable
    public static Switches selectSwitches (int user_id, String DocID) {
        Cursor cursor = db.query("tbSwitches",
                null,
                "idUser = " + user_id + " AND DocID = ?",
                new String[] { DocID },
                null, null, null, null);
        if (cursor.moveToFirst()) {
            Switches switches = new Switches();
            switches.id = cursor.getInt(0);
            switches.idUser = cursor.getInt(1);
            switches.DocID = cursor.getString(2);
            switches.getStarted = cursor.getInt(3);
            switches.finishUnloading = cursor.getInt(4);
            switches.finishWork = cursor.getInt(5);
            switches.valueOdmtr = cursor.getString(6);
            return switches;
        }
        else
            return null;
    }

    public static void deleteSwitches (int user_id, String DocID) {
        if(DocID == null)
            return;
        db.delete(
                "tbSwitches",
                "idUser =" + user_id + " AND DocID =?",
                new String[] { DocID }
        );
    }
}
