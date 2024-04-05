package org.example.Data;

import lombok.*;

import java.time.LocalDate;

@Data
public class Result {
    private LocalDate startDate;
    private LocalDate endDate;
    private Float totalAmount;
    private Float amountToDestination;
    private Float amountToOrigin;

    @Override
    public String toString() {
        return  "\n Total amount: " + totalAmount +
                "\n we start holidays on " + startDate +
                "\n ,and ends on " + endDate +
                "\n ticket to our destination costs: " + amountToDestination +
                "\n and return: " + amountToOrigin;
    }
}
