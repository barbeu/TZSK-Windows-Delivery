package com.example.tzadmin.tzsk_windows.Interface;

import java.util.List;

/**
 * Created by tzadmin on 04.05.17.
 */

public class RouteResponse {
    public List<Route> routes;

    public String getPoints() {
        return this.routes.get(0).overview_polyline.points;
    }

    class Route {
        OverviewPolyline overview_polyline;
    }

    class OverviewPolyline {
        String points;
    }
}
