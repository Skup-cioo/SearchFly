package org.example.Data.RequestModel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Query implements Serializable {
    private List<Location> origins;
    private List<Location> destinations;
}