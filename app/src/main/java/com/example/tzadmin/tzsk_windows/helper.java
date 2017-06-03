package com.example.tzadmin.tzsk_windows;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tzadmin on 07.04.17.
 */

public class helper {

    /*Result DeliveryActivity*/
    public static final int ResetSwithes = 351;

    public static final int True = 1;
    public static final int False = 0;

    /*enum position status "Новая", "В работе", "Исполнена", "Отмена", "Ожидание"*/
    public static final int INDEX_STATUS_NEW = 0;
    public static final int INDEX_STATUS_PROCESS = 1;
    public static final int INDEX_STATUS_COMPLETED = 2;
    public static final int INDEX_STATUS_CANCEL = 3;
    public static final int INDEX_STATUS_WAITING = 4;

    /*enum position global status "Начать Работа", "Закончить Разгрузку", "Закончить Работу"*/
    public static final int INDEX_STATUS_GET_STARTED = 0;
    public static final int INDEX_STATUS_START_UNLOADING = 1;
    public static final int INDEX_STATUS_FINISH_UNLOADING = 2;
    public static final int INDEX_STATUS_FINISH_WORK = 3;

    /*enum codes resp server*/
    public static final int CODE_RESP_SERVER_OK = 200;
    public static final int CODE_RESP_SERVER_AUTH_ERROR = 401;
    public static final int CODE_RESP_SERVER_SERVER_ERROR = 406;
    public static final int CODE_RESP_SERVER_BAD_REQUEST = 400;


    /*enum http params*/
    public static final int HTTP_PARAM_QUERY = 0;
    public static final int HTTP_PARAM_LOGIN = 1;
    public static final int HTTP_PARAM_PASSWORD = 2;
    public static final int HTTP_PARAM_POST_DATA = 3;

    /*enum http query*/
    public static final String HTTP_QUERY_GET_STATUS_PARAM = "param=getStatusParam";
    public static final String HTTP_QUERY_SEND_GLOBAL_STATUS = "param=changeGlobalData";
    public static final String HTTP_QUERY_SEND_PHOTO = "param=photo";
    public static final String HTTP_QUERY_CHANGE_DATA = "param=changeData";
    public static final String HTTP_QUERY_AUTH = "param=auth";
    public static final String HTTP_QUERY_GETORDERS = "param=getclients";

    /*TODO fix config*/
    public static String googleApi = "https://maps.googleapis.com";
    public static String port = "11120";
    public static String httpServer = "http://192.168.0.251/tzsk_tst/hs/JavaMobileAppDelivery/AnyInquiry/?";

    public static void message (Context context, MSG msg, Integer length) {
        String message = null;
        message = msg_switch(msg);
        if(message != null) {
            Toast toast = Toast.makeText(context, message, length);
            toast.show();
        }
    }

    public static void message (Context context, MSG msg, String param, Integer length) {
        String message = null;
        message = msg_switch(msg);
        if(message != null) {
            Toast toast = Toast.makeText(context, message + param, length);
            toast.show();
        }
    }

    private static String msg_switch (MSG msg) {
        String message = null;
        switch (msg) {
            case EMPTY_AUTH_DATA:
                message = "Логин и пароль не может быть пустым";
                break;
            case INTERNET_NOT_CONNECTING:
                message = "Отсутствует интернет подключение";
                break;
            case INCORRECT_AUTH_DATA:
                message = "Логин или пароль неверный";
                break;
            case INCORRECT_RESP_SERVER_DATA:
                message = "От сервера пришли некорректные данные";
                break;
            case INCORRECT_SPINNER_ITEM:
                message = "Неправильный выбор статуса";
                break;
            case POWER_SEND_GEODATA:
                message = "Включите передачу геоданных";
                break;
            case ERROR_COORDINATE_ADDRESS:
                message = "Неправильный адрес доставки";
                break;
            case ERROR_ODMTR_VALUE_NULLABLE:
                message = "Для окончания работы необходимо ввести значение одометра";
                break;
            case ERROR_SERVER_CODE:
                message = "Соединение с сервером невозможно. Код Ошибки - ";
                break;
            case ERROR_DELIVERY_NOT_MET:
                message = "Не все доставки выполнены";
                break;
        }
        return message;
    }

    /*enum show message*/
    public enum MSG {
        EMPTY_AUTH_DATA,
        INTERNET_NOT_CONNECTING,
        INCORRECT_AUTH_DATA,
        INCORRECT_RESP_SERVER_DATA,
        INCORRECT_SPINNER_ITEM,
        POWER_SEND_GEODATA,
        ERROR_COORDINATE_ADDRESS,
        ERROR_ODMTR_VALUE_NULLABLE,
        ERROR_SERVER_CODE,
        ERROR_DELIVERY_NOT_MET
    }

    public static boolean InetHasConnection(final Context context) {
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

    public static String streamToString(InputStream is) {
        if(is == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String Date () {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format( new Date() );
    }

    public static String Date (Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format( date );
    }

    public static int StringToMillisec (String input) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        long timeInMilliseconds = -1;
        try {
            Date mDate = sdf.parse(input);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return (int)timeInMilliseconds;
    }

    public static int getDay (String date) {
        return Integer.parseInt(date.substring(8,10));
    }

    public static int getMonth (String date) {
        return Integer.parseInt(date.substring(5,7));
    }

    public static int getYear (String date) {
        return Integer.parseInt(date.substring(0,4));
    }

    public static void setFilterEditBox (EditText box, int value) {
        int maxLength = value;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        box.setFilters(FilterArray);
    }

}

