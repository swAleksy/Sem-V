package com.wc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

public class Main {
    private static final int NUMBER_OF_THREADS = 12;
    private static Set<String> visitedLinks = ConcurrentHashMap.newKeySet();
    public static void main(String[] args) {
        DBConn dbConn = new DBConn();
        dbConn.connect();
        dbConn.createTables();
        dbConn.insertRow("https://www.uken.krakow.pl/", 0);

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        try {
            while (true) {
                ArrayList<String> links = new ArrayList<>();

                String unseenLink;
                while ((unseenLink = dbConn.getUnseenLink()) != null) {
                    links.add(unseenLink);
                }

                if (links.isEmpty()) {
                    System.out.println("No more unseen links");
                    break;
                }

                ExecutorCompletionService<List<String>> completionService = new ExecutorCompletionService<>(executor);
                for (String link : links) {
                    completionService.submit(new CrawlerThread(link));
                }

                for (int i = 0; i < links.size(); i++) {
                    try {
                        Future<List<String>> future = completionService.take();
                        List<String> newLinks = future.get();
                        for (String newLink : newLinks) {
                            if (addToVisited(newLink)) {
                                dbConn.insertRow(newLink, isInScope(newLink) ? 0 : 1);
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println("Error processing links: " + e.getMessage());
                    }
                }
            }
        } finally {
            executor.shutdown();
            dbConn.disconnect();
        }
        //         List<Future<List<String>>> futures = new ArrayList<>();

        //         for (String link : links) {
        //             CrawlerThread crawlerThread = new CrawlerThread(link);
        //             futures.add(executor.submit(crawlerThread));
        //         }

        //         // Process crawler results
        //         for (Future<List<String>> future : futures) {
        //             try {
        //                 List<String> newLinks = future.get(); 
        //                 for (String newLink : newLinks) {
        //                     if (addToVisited(newLink)) {
        //                         dbConn.insertRow(newLink, isInScope(newLink) ? 0 : 1);
        //                     }
        //                 }
        //             } catch (InterruptedException | ExecutionException e) {
        //                 System.err.println("Error retrieving links: " + e.getMessage());
        //             }
        //         }
        //     }
        // } finally {
        //     executor.shutdown(); 
        //     dbConn.disconnect(); 
        // }
    }

    private static String extractDomain(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost(); 
        } catch (URISyntaxException e) {
            System.err.println("Invalid URL: " + url);
            return null;
        }
    }

    private static boolean isInScope(String url) {
        String domain = extractDomain(url);
        return domain != null && domain.endsWith("uken.krakow.pl");
    }

    private static boolean addToVisited(String url) {
        return visitedLinks.add(url);
    }
}