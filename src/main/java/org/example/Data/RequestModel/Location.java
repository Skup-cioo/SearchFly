package org.example.Data.RequestModel;

import lombok.Data;

import java.io.Serializable;

@Data
public class Location implements Serializable {
    private String code;
    private final String type = "CITY";

    public Location(String code) {
        this.code = code;
    }
}
