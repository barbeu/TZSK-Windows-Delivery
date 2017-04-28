package com.example.tzadmin.tzsk_windows.DatabaseModule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tzadmin on 13.04.17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "dbDeliveries", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tbUsers ("
                + "id integer primary key autoincrement,"
                + "login text,"
                + "password text,"
                + "autoLogin integer" + ");");

        db.execSQL("create table tbDeliveries ("
                + "id integer primary key autoincrement,"
                + "idUser integer,"
                + "DeliveryDate date,"
                + "DocID text,"
                + "SerialNumber text,"
                + "Client text,"
                + "Address text,"
                + "ContactDetails text,"
                + "NumberOfProducts text,"
                + "Task text,"
                + "Mileage text,"
                + "Status integer,"
                + "Summ integer,"
                + "FOREIGN KEY (idUser) REFERENCES tbUsers(id)" + ");");

        db.execSQL("create table tbChangedStatus ("
                + "id integer primary key autoincrement,"
                + "idUser integer,"
                + "DocID text,"
                + "SerialNumber text,"
                + "Status integer,"
                + "Summ integer,"
                + "Date date" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

