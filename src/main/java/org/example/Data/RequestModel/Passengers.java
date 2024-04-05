package org.example.Data.RequestModel;

import lombok.Data;

import java.io.Serializable;

@Data
public class Passengers implements Serializable {
    private int adults;
    private int children;
    private int infants;
    private int youth;

    public Passengers(int adults, int children, int infants, int youth) {
        this.adults = adults;
        this.children = children;
        this.infants = infants;
        this.youth = youth;
    }
}