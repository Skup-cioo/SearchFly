package org.example.Data.RequestModel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FlightQuery implements Serializable {
    private List<Query> queries;
    private String deduplicate;
    private Passengers passengers;

    public FlightQuery(List<Query> queries, Passengers passengers, String deduplicate) {
        this.queries = queries;
        this.passengers = passengers;
        this.deduplicate = deduplicate;
    }
}