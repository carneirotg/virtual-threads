package com.learning.structuredTaskScope;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Stream;

public class BookingFlights {

    public static void main(String[] args) {
        String from = "Amsterdam";
        String to = "Rio de Janeiro";

        var start = Instant.now();
        var flight1 = Flight.readFromAlphaAirlines(from, to);
        var flight2 = Flight.readFromPlanetAirlines(from, to);
        var flight3 = Flight.readFromGlobalAirlines(from, to);
        var end = Instant.now();

        Flight bestFlight = Flight.readFlight(from, to);
//
//        System.out.println("Flight 1 = " + flight1);
//        System.out.println("Flight 2 = " + flight2);
//        System.out.println("Flight 3 = " + flight3);
//
//        Flight bestFlight = Stream.of(flight1, flight2, flight3)
//                .min(Comparator.comparing(Flight::price))
//                .get();

        System.out.println("Best flight = " +  bestFlight +
                " in " + Duration.between(start, end).toMillis());
    }
}
