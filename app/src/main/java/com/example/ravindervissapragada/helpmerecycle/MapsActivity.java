package com.example.ravindervissapragada.helpmerecycle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Ask for location.
        requestLocation();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    protected void requestLocation() {
        LocationRequest lr = LocationRequest.create();
        lr.setInterval(10000);
        lr.setFastestInterval(10000);
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
        Task getLocation = fusedLocationClient.getLastLocation();
        while (!getLocation.isComplete()) {
            System.out.println("Getting location still...");
        }
        if (getLocation.isSuccessful()) {
            System.out.println("Got location!");
            Location location = (Location) getLocation.getResult();
            if (location != null) {
                System.out.println("Not null location!");
                // Create LatLon Object and put it on the map.
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                LatLng latlon = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions()
                        .position(latlon)
                        .title("You Are Here"));
            }
        }
    }
}
