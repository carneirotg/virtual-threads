package com.learning;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ThreadComparisonExample {
    public static void main(String[] args) throws InterruptedException {
        int numRequests = 30;
        String[] urls = { "https://the-one-api.dev/v2/book/", "https://http.cat/200", "https://pokeapi.co/api/v2/pokemon/ditto" };

        long startTime, endTime;

        startTime = System.currentTimeMillis();
        Thread[] platformThreads = new Thread[numRequests];
        for (int i = 0; i < numRequests; i++) {
            final int index = i;
            platformThreads[i] = new Thread(() -> {
                try {
                    String url = urls[index % urls.length];
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    System.out.println("Platform thread: " + url + " - Response code: " + responseCode);
                } catch (IOException e) {
                    //do nothing
                }
            });
            platformThreads[i].start();
        }
        for (Thread thread : platformThreads) {
            thread.join();
        }
        endTime = System.currentTimeMillis();
        System.out.println("Platform threads time: " + (endTime - startTime) + " ms");

        // Using virtual threads
        startTime = System.currentTimeMillis();
        List<Thread> virtualThreads = new ArrayList<>();
        for (int i = 0; i < numRequests; i++) {
            final int index = i;
            Thread virtualThread = Thread.ofVirtual().start(() -> {
                try {
                    String url = urls[index % urls.length];
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    System.out.println("Virtual thread: " + url + " - Response code: " + responseCode);
                } catch (IOException e) {
                    //do nothing
                }
            });
            virtualThreads.add(virtualThread);
        }
        for (Thread thread : virtualThreads) {
            thread.join();
        }
        endTime = System.currentTimeMillis();
        System.out.println("Virtual threads time: " + (endTime - startTime) + " ms");
    }
}
