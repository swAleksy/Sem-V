package com.wc;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Parser;

public class CrawlerThread extends Thread{
    private final String url;
    private List<String> links; 

    public CrawlerThread(String url){
        this.url = url;
        this.links = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements linkElements = doc.select("a[href]");

            for (Element link : linkElements) {
                links.add(link.attr("abs:href"));
            }
        } catch (IOException e) {
            System.err.println("Error fetching links from " + url + ": " + e.getMessage());
        }
    }

    public List<String> getLinks() {
        return links;
    }
}
