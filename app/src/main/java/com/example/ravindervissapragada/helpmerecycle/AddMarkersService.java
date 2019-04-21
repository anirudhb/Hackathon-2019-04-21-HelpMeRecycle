package com.example.ravindervissapragada.helpmerecycle;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

class AddMarkersService extends IntentService {
    @Override
    protected void onHandleIntent(Intent intent) {
        String item = intent.getStringExtra("type");
        double lat = intent.getDoubleExtra("lat");
        double lon = intent.getDoubleExtra("lon");
        Location targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(lat);//your coords of course
        targetLocation.setLongitude(lon);
        try {
            Geocoder coder = new Geocoder(this, Locale.getDefault());
            System.out.println("Creataed geocoder");
            List<String> addresses = DataIntercepter.run(item, getZipCodeFromLocation(coder, targetLocation));
            System.out.println("Ran intercepter");
            List<Address> latlong;
            for (String address: addresses) {
                // Reverse geocode, and put on map.
                latlong = coder.getFromLocationName(address, 5);
                if (latlong == null) { continue; }
                Address l = latlong.get(0);
                LatLng pt = new LatLng(l.getLatitude(), l.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(pt)
                        .title("Another area"));
                System.out.println("Plotted point");
            }
        } catch (IOException e) {
            System.out.printf("Error: %s\n", e.getLocalizedMessage());
        }
    }
}
