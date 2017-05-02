package com.example.tzadmin.tzsk_windows.JsonModule;

import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.ChangedData;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by tzadmin on 15.04.17.
 */

public class JSON {

    public static ArrayList<Delivery> parse (String json) {
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
                deliveries.add(delivery);
            }
        } catch (JSONException e) {
            return null;
        }
        return deliveries;
    }

    public static String generateClients (ArrayList<Delivery> deliveries) {
        if(deliveries == null)
            return null;
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
            dataJsonObj.put("arrayOfClients", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        res = dataJsonObj.toString();
        return res;
    }

    public static String generateChangedData (ArrayList<ChangedData> statuses) {
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

    public static String generatePhoto (String photo, String DocID) {
        JSONObject dataJsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String result = null;
        try {
            JSONArray json = new JSONArray();
            json.put(DocID);
            json.put(photo);
            jsonArray.put(json);
            dataJsonObj.put("arrayOfPhoto", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        result = dataJsonObj.toString();
        return result;
    }
}
