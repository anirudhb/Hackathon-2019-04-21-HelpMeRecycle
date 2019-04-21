package com.example.ravindervissapragada.helpmerecycle;

import android.location.Geocoder;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
public class DataIntercepter extends MapsActivity{
    public static void main(String[] args) throws IOException {
        DataIntercepter di = new DataIntercepter();
        di.run();

    }
    public void run(){
        Geocoder geocoder = new Geocoder();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String item = extras.getString("type");
            String zipcode = geocoder.getFromLocation(lat,lon, 1);
        }

        Document doc = Jsoup.connect("http://search.earth911.com/?what="+).get();
        log(doc.title());

        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            log("%s\n\t%s", headline.attr("title"), headline.absUrl("href"));
        }
    }

    private static void log(String msg, String... vals) {
        System.out.println(String.format(msg, vals));
    }

}
