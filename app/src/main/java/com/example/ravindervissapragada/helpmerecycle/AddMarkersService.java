package com.example.ravindervissapragada.helpmerecycle;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddMarkersService extends IntentService {
    final static String RES_KEY = "res";
    final static String RES2_KEY = "res2";

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
        GeoApiContext ctxt = new GeoApiContext.Builder()
                .apiKey(getString(R.string.key))
                .build();
        Location targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(lat);//your coords of course
        targetLocation.setLongitude(lon);
        try {
            Geocoder coder = new Geocoder(this, Locale.getDefault());
            System.out.println("Creataed geocoder");
            List<Pair<String, String>> addresses = DataIntercepter.run(item, getZipCodeFromLocation(coder, targetLocation));
            System.out.println("Ran intercepter");
            ArrayList<LatLng> pts = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();
            for (Pair<String, String> p: addresses) {
                String address = p.first;
                String title = p.second;
                // Reverse geocode, and put on map.
                System.out.printf("Address: %s\n", address);
                if (address.isEmpty()) continue;
                if (address.trim().isEmpty()) continue;
                System.out.println("Running request");
                //GeocodingResult[] latlong = GeocodingApi.newRequest(ctxt).address(address).await();
                //if (latlong == null || latlong.length == 0) { continue; }
                //com.google.maps.model.LatLng _loc = latlong[0].geometry.location;
                List<Address> loc = coder.getFromLocationName(address, 5);
                if (loc == null) continue;
                Address a = loc.get(0);
                LatLng pt = new LatLng(a.getLatitude(), a.getLongitude());
                pts.add(pt);
                titles.add(title);
            }
            System.out.println("Success");
            deliverResultToReceiver(0, pts, titles);
        } catch (Exception e) {
            System.out.printf("Error: %s\n", e.getLocalizedMessage());
        }
    }

    private void deliverResultToReceiver(int resultCode, ArrayList<LatLng> message, ArrayList<String> titles) {
        Intent intent = new Intent();
        intent.setAction("SUCCESS");
        intent.putExtra(RES_KEY, message);
        intent.putExtra(RES2_KEY, titles);
        System.out.println("sending broadcast");
        sendBroadcast(intent);
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
