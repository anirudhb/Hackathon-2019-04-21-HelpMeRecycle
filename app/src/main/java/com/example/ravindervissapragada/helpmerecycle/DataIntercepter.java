package com.example.ravindervissapragada.helpmerecycle;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class DataIntercepter{
    /* not necessary, we will be instantiating this from the activity
    public static void main(String[] args) throws IOException {
        DataIntercepter di = new DataIntercepter();
        di.run();

    }
    */
    // Returns the addresses nearby.
    public static List<String> run(String what, String zipcode) throws IOException {
        String url = String.format(Locale.ENGLISH, "https://search.earth911.com/?what=%s&where=%s", what, zipcode);
        System.out.printf("URL: %s\n", url);
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.title());

        List<String> addresses = new ArrayList<String>();
        Elements locations = doc.select(".location");
        for (Element location: locations) {
            Elements contacts = location.select(".address1, .address2, .address3");
            // Contatenate their contents.
            List<String> addresstxts = new ArrayList<String>();
            for (Element contact: contacts) {
                addresstxts.add(contact.ownText());
                System.out.printf("Contact: %s\n", contact.ownText());
            }
            String address = String.join(" ", addresstxts);
            addresses.add(address);
        }
        for(int i = 0; i<addresses.size(); i++) {
            if((addresses.get(i).isEmpty()))
            {
                addresses.remove(i);
                //startActivity(map);
            }
        }
        return addresses;
    }
}
