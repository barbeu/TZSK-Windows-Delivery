package com.example.tzadmin.tzsk_windows;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.InterfaceRoute.RouteApi;
import com.example.tzadmin.tzsk_windows.InterfaceRoute.RouteResponse;
import com.example.tzadmin.tzsk_windows.Location.MyLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import java.util.List;

import retrofit.RestAdapter;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Delivery delivery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intentGetId = getIntent();
        int id = intentGetId.getIntExtra("id", -1);
        delivery = Database.selectDelivery(id, Auth.id);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String position = MyLocation.Latitude + "," + MyLocation.Longitude;
        String destination = "54.2087361,37.661695";
        new get().execute(position, destination);
    }

    class get extends AsyncTask<String, Void, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(String... params) {

            RestAdapter restAdapter =
                    new RestAdapter.Builder()
                    .setEndpoint(helper.googleApi)
                    .build();

            RouteApi routeService = restAdapter.create(RouteApi.class);
            RouteResponse routeResponse = routeService.getRoute(params[0], params[1], true, "ru");
            List<LatLng> mPoints = PolyUtil.decode(routeResponse.getPoints());

            return mPoints;
        }

        @Override
        protected void onPostExecute (List<LatLng> result) {

            PolylineOptions line = new PolylineOptions();
            line.width(10f).color(R.color.colorAccent);
            LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
            for (int i = 0; i < result.size(); i++) {
                if (i == 0) {
                    MarkerOptions startMarkerOptions = new MarkerOptions()
                            .position(result.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_start_loc));
                    mMap.addMarker(startMarkerOptions);
                } else if (i == result.size() - 1) {
                    MarkerOptions endMarkerOptions = new MarkerOptions()
                            .position(result.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_end_loc));
                    mMap.addMarker(endMarkerOptions);
                }
                line.add(result.get(i));
                latLngBuilder.include(result.get(i));
            }
            mMap.addPolyline(line);
            int size = getResources().getDisplayMetrics().widthPixels;
            LatLngBounds latLngBounds = latLngBuilder.build();
            CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 12);
            mMap.moveCamera(track);
        }
    }

}
