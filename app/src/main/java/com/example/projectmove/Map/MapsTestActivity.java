package com.example.projectmove.Map;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.projectmove.R;
import com.example.projectmove.Utilis.Class.Map.FetchURL;
import com.example.projectmove.Utilis.Class.Map.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsTestActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    MarkerOptions place1, place2;
    Polyline currentPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_test);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        place1=new MarkerOptions().position(new LatLng(27.658143,85.3199503)).title("location1");
        place2=new MarkerOptions().position(new LatLng(27.667491,85.3208503)).title("location2");

        String url=getUrl(place1.getPosition(),place2.getPosition(),"driving");
        new FetchURL(MapsTestActivity.this).execute(url,"driving");

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

        Log.d("mylog", "Added Markers");
        mMap.addMarker(place1);
        mMap.addMarker(place2);


    }

    private String getUrl(LatLng origin,LatLng dest, String directionMode){
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyBGMdqoREyEFOze-G7SvQ16SVTTxvGwS3g";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}