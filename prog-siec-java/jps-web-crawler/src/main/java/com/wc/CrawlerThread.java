package com.wc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        System.out.println(url);
        this.links = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements linkElements = doc.select("a[href]");
            System.out.println(linkElements);
            for (Element link : linkElements) {
                String newLink = link.attr("abs:href");
                if (!shouldSkip(newLink)) {
                    links.add(newLink);
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching links from " + url + ": " + e.getMessage());
        }

        return links;
    }
    private boolean shouldSkip(String url) {
        return url.startsWith("mailto:") || url.startsWith("tel:") || url.startsWith("javascript:");
    }
}