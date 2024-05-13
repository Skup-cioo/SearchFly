package org.example.Data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Uri {
    BASE("https://api.ith.toys"),
    FRU("https://www.fru.pl/");
    private String value;
}
