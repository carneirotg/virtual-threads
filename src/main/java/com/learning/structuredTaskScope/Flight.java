package com.learning.structuredTaskScope;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

public record Flight(String from, String to, int price, String airline) {

    private static final Random random = new Random();

    private static class FlightScope extends StructuredTaskScope<Flight> {

        private volatile Collection<Flight> flights = new ConcurrentLinkedDeque<>();
        private volatile Collection<Throwable> exceptions = new ConcurrentLinkedDeque<>();

        @Override
        protected void handleComplete(Subtask<? extends Flight> subtask) {
            switch (subtask.state()) {
                case UNAVAILABLE -> throw new IllegalStateException("Task should be done");
                case SUCCESS ->  this.flights.add(subtask.get());
                case FAILED -> this.exceptions.add(subtask.exception());
            }
        }

        public Flight bestFlight() {
            return this.flights.stream()
                    .min(Comparator.comparingInt(Flight::price))
                    .orElseThrow();
        }
    }

    public static Flight readFlight(String from, String to) {

        // Traditional Concurrency with ExecutorService
//        try (var executor = new ScheduledThreadPoolExecutor(3)) {
//            var t1 = executor.submit(() -> Flight.readFromAlphaAirlines(from, to));
//            var t2 = executor.submit(() -> Flight.readFromPlanetAirlines(from, to));
//            var t3 = executor.submit(() -> Flight.readFromGlobalAirlines(from, to));
//
//            var bestFlight = Stream.of(t1.get(), t2.get(), t3.get())
//                    .min(Comparator.comparing(Flight::price))
//                    .get();
//
//            System.out.println("best price -> " + bestFlight);
//
//        } catch (ExecutionException ex) {
//            throw new RuntimeException(ex);
//        } catch (InterruptedException ex) {
//            throw new RuntimeException(ex);
//        }

        //Concurrency with StructuredTaskScope
//        FlightQuery query = new FlightQuery(from, to);
//
//        try (var scope = new StructuredTaskScope<Flight>()) {
//            var t1 = scope.fork(query::readFromAlphaAirlines);
//            var t2 = scope.fork(query::readFromPlanetAirlines);
//            var t3 = scope.fork(query::readFromGlobalAirlines);
//
//            scope.join();
//
//            Flight bestFlight = Stream.of(t1, t2, t3)
//                    .filter(t -> t.state() == StructuredTaskScope.Subtask.State.SUCCESS)
//                    .map(StructuredTaskScope.Subtask::get)
//                    .min(Comparator.comparing(Flight::price))
//                    .get();
//
////            return bestFlight;
//
//            return scope.bestFlight();
//        }
        return null;
    }
    public static Flight readFromAlphaAirlines(String from, String to) {
        sleepFor(random.nextInt(80,100), ChronoUnit.MILLIS);
        return new Flight(from, to,
                random.nextInt(79,120),
                "Alpha Airlines");
    }

    public static Flight readFromPlanetAirlines(String from, String to) {
        sleepFor(random.nextInt(90,110), ChronoUnit.MILLIS);
        return new Flight(from, to,
                random.nextInt(60,90),
                "Global Airlines");
    }

    public static Flight readFromGlobalAirlines(String from, String to) {
        sleepFor(random.nextInt(70,120), ChronoUnit.MILLIS);
        return new Flight(from, to,
                random.nextInt(70,90),
                "Planet Airlines");
    }

    private static void sleepFor(int i, ChronoUnit chronoUnit) {
        try {
            Thread.sleep(Duration.of(i, chronoUnit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
