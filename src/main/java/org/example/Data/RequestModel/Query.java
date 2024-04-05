package org.example.Data.RequestModel;

import lombok.Data;

import java.io.Serializable;

@Data
public class Query implements Serializable {
    private String departure;
    private Location origin;
    private Location destination;
}