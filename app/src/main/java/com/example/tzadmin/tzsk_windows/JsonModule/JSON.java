package com.example.tzadmin.tzsk_windows.JsonModule;

import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.ChangedData;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Photo;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.StatusParam;
import com.example.tzadmin.tzsk_windows.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tzadmin on 15.04.17.
 */

public class JSON {

    public static ArrayList<Delivery> parseDeliveries (String json) {
        if(json == null)
            return null;
        ArrayList<Delivery> deliveries = new ArrayList<>();
        try {
            JSONObject dataJsonObj = new JSONObject(json);
            JSONArray orders = dataJsonObj.getJSONArray("arrayOfClients");
            for (int i=0; i< orders.length(); i++) {
                JSONObject order = orders.getJSONObject(i);
                Delivery delivery = new Delivery();
                delivery.idUser = Auth.id;
                delivery.DeliveryDate = order.getString("DeliveryDate");
                //date to day - month - year
                String date = delivery.DeliveryDate;
                delivery.year = helper.getYear(date);
                delivery.month = helper.getMonth(date);
                delivery.day = helper.getDay(date);
                //
                delivery.DocID = order.getString("DocID");
                delivery.SerialNumber = order.getString("SerialNumber");
                delivery.Client = order.getString("Client");
                delivery.Address = order.getString("Address");
                delivery.ContactDetails = order.getString("ContactDetails");
                delivery.NumberOfProducts = order.getString("NumberOfProducts");
                delivery.Task = order.getString("Task");
                delivery.Mileage = order.getString("Mileage");
                delivery.Status = order.getInt("Status");
                delivery.Summ = order.getInt("Summ");
                delivery.lati = order.getString("Latitude");
                delivery.longi = order.getString("Longitude");
                deliveries.add(delivery);
            }
        } catch (JSONException e) {
            return null;
        }
        return deliveries;
    }

    public static StatusParam parseStatusParam (String json) {
        if(json == null)
            return null;
        StatusParam statusParam = new StatusParam();
        try {
            JSONObject dataJsonObj = new JSONObject(json);
            JSONArray orders = dataJsonObj.getJSONArray("arrayOfStatusParam");
            JSONObject order = orders.getJSONObject(0);
            statusParam.idUser = Auth.id;
            statusParam.DocID = order.getString("DocID");
            statusParam.AllMileage = order.getInt("AllMileage");
            statusParam.AllOdmtr = order.getInt("AllOdmtr");
        } catch (JSONException e) {
            return null;
        }
        return statusParam;
    }

    public static String generateClients (ArrayList<Delivery> deliveries, String date) {
        if(deliveries == null) {
            JSONObject dataJsonObj = new JSONObject();
            JSONArray arrayDate = new JSONArray();
            arrayDate.put(date);
            try {
                dataJsonObj.put("Date", arrayDate);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
            return dataJsonObj.toString();
        } else {
            JSONObject dataJsonObj = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            String res = null;
            try {
                for (int i = 0; i < deliveries.size(); i++) {
                    JSONArray array = new JSONArray();
                    array.put(deliveries.get(i).DocID.toString());
                    array.put(deliveries.get(i).SerialNumber.toString());
                    jsonArray.put(array);
                }
                JSONArray arrayDate = new JSONArray();
                arrayDate.put(date);

                dataJsonObj.put("Date", arrayDate);
                dataJsonObj.put("arrayOfClients", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
            res = dataJsonObj.toString();
            return res;
        }
    }

    public static String generateNoGlobalStatus (ArrayList<ChangedData> statuses) {
        if(statuses == null)
            return null;

        JSONObject dataJsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String res = null;
        try {
            for (int i = 0; i < statuses.size(); i++){
                JSONArray array = new JSONArray();
                array.put(statuses.get(i).DocID.toString());
                array.put(statuses.get(i).SerialNumber.toString());
                array.put(String.valueOf(statuses.get(i).Status));
                array.put(String.valueOf(statuses.get(i).summ));
                array.put(statuses.get(i).Date.toString());
                jsonArray.put(array);
            }
            dataJsonObj.put("arrayOfData", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        res = dataJsonObj.toString();
        return res;
    }

    public static String generateGlobalStatus (ArrayList<ChangedData> statuses) {
        if(statuses == null)
            return null;

        JSONObject dataJsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String res = null;
        try {
            for (int i = 0; i < statuses.size(); i++){
                JSONArray array = new JSONArray();
                array.put(statuses.get(i).DocID.toString());
                array.put(String.valueOf(statuses.get(i).Status));
                if(statuses.get(i).summ != -1)
                    array.put(statuses.get(i).summ);
                array.put(statuses.get(i).Date.toString());
                jsonArray.put(array);
            }
            dataJsonObj.put("arrayOfGlobalData", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        res = dataJsonObj.toString();
        return res;
    }

    public static String generateKeysPhoto(ArrayList<Photo> photos) {
        JSONObject dataJsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String result = null;
        try {
            for (Photo photo : photos) {
                jsonArray.put(photo.DocID + "&" + photo.SerialNumber);
                jsonArray.put(photo.date);
            }
            dataJsonObj.put("arrayOfKey", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = dataJsonObj.toString();
        return result;
    }

    public static String generateStatusParam (String DocID) {
        JSONObject dataJsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String result = null;
        try {
            jsonArray.put(DocID);
            dataJsonObj.put("arrayOfDocID", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = dataJsonObj.toString();
        return result;
    }

    public static String parseDocID (String json) {
        if(json == null)
            return null;
        Delivery delivery = new Delivery();
        try {
            JSONObject dataJsonObj = new JSONObject(json);
            JSONArray orders = dataJsonObj.getJSONArray("arrayOfClients");
            JSONObject order = orders.getJSONObject(0);
            delivery.DocID = order.getString("DocID");
        } catch (JSONException e) {
            return null;
        }
        return delivery.DocID;
    }
}
