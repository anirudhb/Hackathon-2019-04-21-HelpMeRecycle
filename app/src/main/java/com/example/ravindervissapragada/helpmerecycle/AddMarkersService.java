package com.example.ravindervissapragada.helpmerecycle;

class AddMarkersService extends IntentService {
    @Override
    protected void onHandleIntent(Intent intent) {
        String item = getIntent().getStringExtra("type");
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
