package com.learning;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try {
            Callable<String> task = () -> {
                var request = HttpRequest.newBuilder()
                        .uri(new URI("https://the-one-api.dev/v2/book/"))
                        .GET()
                        .build();
                var response = HttpClient.newBuilder()
                        .build()
                        .send(request, HttpResponse.BodyHandlers.ofString());


                return response.body();
            };

            Future<String> future =
                    Executors.newFixedThreadPool(3)
                            .submit(task);


            var threadId = Thread.currentThread().threadId();
            System.out.println(future.get());
        } catch (Exception e) {
            //do nothing
        }
    }
}