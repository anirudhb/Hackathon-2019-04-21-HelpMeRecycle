package com.example.ravindervissapragada.helpmerecycle;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
public class DataIntercepter{
    /* not necessary, we will be instantiating this from the activity
    public static void main(String[] args) throws IOException {
        DataIntercepter di = new DataIntercepter();
        di.run();

    }
    */
    public static void run(String what, String zipcode, Context context) throws {
        Geocoder geocoder = new Geocoder(context);


        String url = String.format("http://search.earth911.com/?what=%s&where=%d", what, zipcode);
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.title());

        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
                System.out.printf("%s\n\t%s\n", headline.attr("title"), headline.absUrl("href"));
        }
    }
}
