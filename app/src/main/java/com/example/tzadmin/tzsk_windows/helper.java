package com.example.tzadmin.tzsk_windows;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tzadmin on 07.04.17.
 */

public class helper {

    /*enum position status "Новая", "В работе", "Исполнена", "Отмена", "Ожидание"*/
    public static final int INDEX_STATUS_NEW = 0;
    public static final int INDEX_STATUS_PROCESS = 1;
    public static final int INDEX_STATUS_COMPLETED = 2;
    public static final int INDEX_STATUS_CANCEL = 3;
    public static final int INDEX_STATUS_WAITING = 4;

    /*enum codes resp server*/
    public static final int CODE_RESP_SERVER_OK = 200;
    public static final int CODE_RESP_SERVER_AUTH_ERROR = 401;
    public static final int CODE_RESP_SERVER_SERVER_ERROR = 500;
    public static final int CODE_RESP_SERVER_BAD_REQUEST = 400;

    /*enum http params*/
    public static final int HTTP_PARAM_QUERY = 0;
    public static final int HTTP_PARAM_LOGIN = 1;
    public static final int HTTP_PARAM_PASSWORD = 2;
    public static final int HTTP_PARAM_POST_DATA = 3;

    /*enum http query*/
    public static final String HTTP_QUERY_AUTH = "param=auth";
    public static final String HTTP_QUERY_GETORDERS = "param=getclients";

    /*TODO fix config*/
    public static String port = "11120";
    public static String httpServer = "http://192.168.0.251/tzsk_tst/hs/JavaMobileAppDelivery/AnyInquiry/?";

    public static void message (Context context, MSG msg, Integer length) {
        String message = null;
        switch (msg) {
            case EMPTY_AUTH_DATA:
                message = "Логин и пароль не может быть пустым.";
                break;
            case INTERNET_NOT_CONNECTING:
                message = "Отсутствует интернет подключение.";
                break;
            case INCORRECT_AUTH_DATA:
                message = "Логин или пароль неверный.";
                break;
            case INCORRECT_RESP_SERVER_DATA:
                message = "От сервера пришли некорректные данные.";
                break;
        }
        Toast toast = Toast.makeText(context, message, length);
        toast.show();
    }

    /*enum show message*/
    public enum MSG {
        EMPTY_AUTH_DATA,
        INTERNET_NOT_CONNECTING,
        INCORRECT_AUTH_DATA,
        INCORRECT_RESP_SERVER_DATA
    }

    public static boolean InetHasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static String streamToString(InputStream is) throws IOException {
        if(is == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
