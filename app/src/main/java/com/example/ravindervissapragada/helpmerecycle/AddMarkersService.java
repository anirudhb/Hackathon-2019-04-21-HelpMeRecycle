package com.example.ravindervissapragada.helpmerecycle;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class AddMarkersService extends IntentService {
    final static String RES_KEY = "res";

    protected ResultReceiver receiver;

    public AddMarkersService(String s) {
        super(s);
    }

    public AddMarkersService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String item = intent.getStringExtra("type");
        double lat = intent.getDoubleExtra("lat", 0);
        double lon = intent.getDoubleExtra("lon", 0);
        receiver = intent.getParcelableExtra("receiver");
        Location targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(lat);//your coords of course
        targetLocation.setLongitude(lon);
        try {
            Geocoder coder = new Geocoder(this, Locale.getDefault());
            System.out.println("Creataed geocoder");
            List<String> addresses = DataIntercepter.run(item, getZipCodeFromLocation(coder, targetLocation));
            System.out.println("Ran intercepter");
            List<Address> latlong;
            ArrayList<LatLng> pts = new ArrayList<LatLng>();
            for (String address: addresses) {
                // Reverse geocode, and put on map.
                latlong = coder.getFromLocationName(address, 5);
                if (latlong == null) { continue; }
                Address l = latlong.get(0);
                LatLng pt = new LatLng(l.getLatitude(), l.getLongitude());
                pts.add(pt);
            }
            deliverResultToReceiver(0, pts);
        } catch (IOException e) {
            System.out.printf("Error: %s\n", e.getLocalizedMessage());
            deliverResultToReceiver(1, null);
        }
    }

    private void deliverResultToReceiver(int resultCode, ArrayList<LatLng> message) {
        Bundle b = new Bundle();
        b.putParcelableArrayList(RES_KEY, message);
        receiver.send(resultCode, b);
    }

    private String getZipCodeFromLocation(Geocoder geocoder, Location location) {
        Address addr = getAddressFromLocation(geocoder, location);
        return addr.getPostalCode() == null ? "" : addr.getPostalCode();
    }
    private Address getAddressFromLocation(Geocoder geocoder, Location location) {
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
