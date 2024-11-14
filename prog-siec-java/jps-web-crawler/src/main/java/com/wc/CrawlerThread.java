package com.wc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.Callable;


public class CrawlerThread implements Callable<List<String>>{
    private final String url;
    private List<String> links; 

    public CrawlerThread(String url){
        this.url = url;
    }

    @Override
    public List<String> call() {
        this.links = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements linkElements = doc.select("a[href]");

            for (Element link : linkElements) {
                links.add(link.attr("abs:href"));
            }
        } catch (IOException e) {
            System.err.println("Error fetching links from " + url + ": " + e.getMessage());
        }

        return links;
    }
}
