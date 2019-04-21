package com.example.ravindervissapragada.helpmerecycle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private int locationRequestCode = 1000;
    private double lat, lon;
    private boolean hasLatLon = false;
    private boolean mapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Ask for permission if not already granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, locationRequestCode);
        } else {
            addLocationListener();
            enableMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addLocationListener();
                    enableMyLocation();
                }
                break;
        }
    }

    protected void addLocationListener() {
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location l) {
                    if (l != null) {
                        lat = l.getLatitude();
                        lon = l.getLongitude();
                        hasLatLon = true;
                        putMarker();
                    }
                }});
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
        mapReady = true;
        enableMyLocation();
        putMarker();
    }

    protected void enableMyLocation() {
        if (mapReady) {
            mMap.setMyLocationEnabled(true);
        }
    }

    protected void putMarker() {
        if (!mapReady || !hasLatLon) return;
        // Create LatLon Object and put it on the map.
        LatLng latlon = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions()
                .position(latlon)
                .title("You Are Here"));
        Bundle extras = getIntent().getExtras();
        String item = "";
        if (extras != null) {
            item = extras.getString("type");
        }
        Location targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(lat);//your coords of course
        targetLocation.setLongitude(lon);

        try {
            Geocoder coder = new Geocoder(this);
            System.out.println("Creataed geocoder");
            List<String> addresses = DataIntercepter.run(item, getZipCodeFromLocation(targetLocation));
            System.out.println("Ran intercepter");
            List<Address> latlong;
            for (String address: addresses) {
                // Reverse geocode, and put on map.
                latlong = coder.getFromLocationName(address, 5);
                if (address == null) { continue; }
                Address l = address.get(0);
                LatLng pt = new LatLng(l.getLatitude(), l.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(latlon)
                        .title("Another area"));
                System.out.println("Plotted point");
            }
        } catch (IOException e) {
            System.out.printf("Error!%s\n", e.getMessage());
        }
    }
    private String getZipCodeFromLocation(Location location) {
        Address addr = getAddressFromLocation(location);
        return addr.getPostalCode() == null ? "" : addr.getPostalCode();
    }
    private Address getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this);
        Address address = new Address(Locale.getDefault());
        try {
            List<Address> addr = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addr.size() > 0) {
                address = addr.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
}
