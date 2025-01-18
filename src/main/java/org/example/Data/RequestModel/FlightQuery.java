package org.example.Data.RequestModel;

import lombok.Data;

import java.io.Serializable;

@Data
public class FlightQuery implements Serializable {
    private Dates dates;
    private Query locations;
    private String deduplicate;
    private Passengers passengers;

    public FlightQuery(Dates dates, Query locations, Passengers passengers, String deduplicate) {
        this.dates = dates;
        this.locations = locations;
        this.passengers = passengers;
        this.deduplicate = deduplicate;
    }
}