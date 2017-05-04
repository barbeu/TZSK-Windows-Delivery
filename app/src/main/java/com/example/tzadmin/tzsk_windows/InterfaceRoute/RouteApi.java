package com.example.tzadmin.tzsk_windows.InterfaceRoute;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by tzadmin on 04.05.17.
 */

public interface RouteApi {
    @GET("/maps/api/directions/json")
    RouteResponse getRoute(
            @Query(value = "origin", encodeValue = false) String position,
            @Query(value = "destination", encodeValue = false) String destination,
            @Query("sensor") boolean sensor,
            @Query("language") String language);
}
