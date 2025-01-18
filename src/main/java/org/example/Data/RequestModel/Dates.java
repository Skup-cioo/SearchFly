package org.example.Data.RequestModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dates {

    private String departureFrom;
    private String departureTo;
    private String returnFrom;
    private String returnTo;
}
