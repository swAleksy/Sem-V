package com.wc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    private static final int NUMBER_OF_THREADS = 10;
    public static void main(String[] args) {
        DBConn dbConn = new DBConn();
        dbConn.connect();
        dbConn.createTables();
        dbConn.insertRow("https://ii.uken.krakow.pl/");

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        List<Future<List<String>>> futures = new ArrayList<>();
        ArrayList<String> links = new ArrayList<String>();

        boolean nextLink = true;
        while (nextLink) {
            while(dbConn.getUnseenLink() != null){
                links.add(dbConn.getUnseenLink());
            }

            for (String link : links){
                CrawlerThread crawlerThread = new CrawlerThread(link);
                Future<List<String>> future = executor.submit(crawlerThread);
                futures.add(future);
            }

            List<Future<List<String>>> completedFutures = new ArrayList<>();
            for (Future<List<String>> future : futures) {
                List<String> newLinks = new ArrayList<>();
                if (future.isDone()) {
                    try {
                        newLinks = future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println("Error retrieving links: " + e.getMessage());
                    }
                    completedFutures.add(future);

                    for (String link : newLinks){
                        dbConn.insertRow(link);
                    }
                }
            }
        }
    }
}