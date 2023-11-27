package com.example.projectmove.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmove.Utilis.Class.LocationHelper;
import com.example.projectmove.databinding.ActivityMapsCustomerBinding;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.projectmove.R;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.internal.connection.RouteException;

public class MapsCustomerActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private SupportMapFragment mapFragment;
    private String customerId="";
    private Boolean isLoggingOut=false;
    private Marker currentLocationMarker;
    private Polyline shortestPathLine;
    private LatLng mDestinationLatLng;
    private Polyline mPolyline;

    Double latitude;
    Double longitude;

    private LatLng currentLocation; // Current location coordinates
    private LatLng markedLocation; // Marked location coordinates
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_customer);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsCustomerActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }
        else{
            mapFragment.getMapAsync(this);
        }




        //get request customer id
        //getAssignedCustomer();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Current Location");
        ValueEventListener valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                 latitude=snapshot.child("latitude").getValue(Double.class);
                 longitude=snapshot.child("longitude").getValue(Double.class);

                LatLng latLng=new LatLng(latitude,longitude);
                mDestinationLatLng=new LatLng(24,90);

                // Check if the currentLocationMarker already exists
                if (currentLocationMarker != null) {
                    // If yes, update its position
                    currentLocationMarker.setPosition(latLng);
                } else {
                    // If not, create a new marker
                    currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                }
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14F));
                drawShortestPath();


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            return;
        }

        buildGoogleApiClient();
        //adding my location
        mMap.setMyLocationEnabled(true);



    }
    private void drawShortestPathLine() {

        markedLocation = new LatLng(latitude, longitude);
        // Create a PolylineOptions object to configure the polyline
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(currentLocation) // Start point of the polyline
                .add(markedLocation) // End point of the polyline
                .color(Color.BLUE) // Set the color of the polyline
                .width(5f); // Set the width of the polyline

        // Add the polyline to the map
        shortestPathLine = mMap.addPolyline(polylineOptions);
    }

    protected synchronized void buildGoogleApiClient(){
        //set the google api client value
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged( Location location) {
        //getting Updated location
        if(getApplicationContext()!=null) {

            mLastLocation = location;


            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocation=new LatLng(location.getLatitude(), location.getLongitude());

            LocationHelper locationHelper=new LocationHelper(
                    location.getLongitude(),
                    location.getLatitude()
            );

            FirebaseDatabase.getInstance().getReference("Current Location")
                    .setValue(locationHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MapsCustomerActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(MapsCustomerActivity.this, "No", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            drawShortestPath();



            //change camera view

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));



        }
    }

    private void drawShortestPath() {
        if (mMap == null || currentLocationMarker == null || mDestinationLatLng == null) {
            return;
        }

        // Clear previous polyline
        if (mPolyline != null) {
            mPolyline.remove();
        }

        // Create list of LatLng representing the shortest path
        List<LatLng> path = calculateShortestPath(); // Replace with your own logic to calculate the shortest path

        // Draw polyline on the map
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(path)
                .width(5)
                .color(Color.BLUE);
        mPolyline = mMap.addPolyline(polylineOptions);

    }

    private List<LatLng> calculateShortestPath() {
        // Replace with your own logic to calculate the shortest path
        // This can be done using any routing algorithm, such as Dijkstra's or A* algorithm
        // Here is an example of creating a list of LatLng representing a simple straight line path

        List<LatLng> path = new ArrayList<>();
        LatLng currentLocation = currentLocationMarker.getPosition();
        path.add(currentLocation);
        path.add(mDestinationLatLng);

        return path;
    }


        @Override
    public void onConnected( Bundle bundle) {
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsCustomerActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    final int LOCATION_REQUEST_CODE=1;
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mapFragment.getMapAsync(this);
                }
                else{
                    Toast.makeText(getApplicationContext(),"plz provide the permission",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
