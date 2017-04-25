package com.example.tzadmin.tzsk_windows.JsonModule;

import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
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
                delivery.Summ = 0;
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
            for (int i = 0; i < deliveries.size(); i++){
                ArrayList arrayList = new ArrayList();
                arrayList.add(deliveries.get(i).DocID.toString());
                arrayList.add(deliveries.get(i).SerialNumber.toString());
                jsonArray.put(arrayList);
            }
            dataJsonObj.put("arrayOfClients", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        res = dataJsonObj.toString();
        return res;
    }
}
