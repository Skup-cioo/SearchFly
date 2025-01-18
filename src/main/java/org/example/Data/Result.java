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
        return  "\n Calkowita Kwota " + totalAmount +
                "\n Lot docelowy jest " + startDate +
                "\n ,a konczy sie " + endDate +
                "\n bilety w tamta strone kosztuja: " + amountToDestination +
                "\n i powrotne: " + amountToOrigin;
    }
}
