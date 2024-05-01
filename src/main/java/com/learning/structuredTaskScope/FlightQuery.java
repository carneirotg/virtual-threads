package com.learning.structuredTaskScope;

public record FlightQuery(String from, String to) {

    public Flight readFromGlobalAirlines() {
        return Flight.readFromGlobalAirlines(from, to);
    }

    public Flight readFromPlanetAirlines() {
        return Flight.readFromGlobalAirlines(from, to);
    }

    public Flight readFromAlphaAirlines() {
        return Flight.readFromGlobalAirlines(from, to);
    }
}
