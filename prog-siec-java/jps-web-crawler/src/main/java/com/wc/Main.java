package com.wc;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        String url = "https://ii.uken.krakow.pl/";
        CrawlerThread crawlerThread = new CrawlerThread(url);

        crawlerThread.start();

        try {
            crawlerThread.join(); 
        } catch (InterruptedException e) {
            System.err.println("Crawler thread was interrupted: " + e.getMessage());
        }

        List<String> links = crawlerThread.getLinks();
        for (String i : links){
            System.out.println(i);
        }
        
        // DBConn dbConn = new DBConn();
        
        // dbConn.connect();
        // dbConn.createTables();
        
        // dbConn.insertRow("https://example.com", false, 1);
        // dbConn.insertRow("https://another-example.com", true, 2);
        
        // dbConn.displayRows();
        
        // dbConn.dropTables();
        // dbConn.disconnect();
    }
}