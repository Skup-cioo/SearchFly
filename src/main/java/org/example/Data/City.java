package org.example.Data;


import lombok.Getter;

@Getter
public enum City {
    //http://www.lotnictwocywilne.pl/kodylotnisk.html -> code of airport
    WARSAW("WAW", "WARSAW"),
    PARIS("PAR", "PARIS"),
    MALLORCA("PMI", "MALLORCA"),
    CYPRUS("PFO", "CYPRUS"),
    BARCELONA("BCN", "BARCELONA"),
    AMSTERDAM("AMS", "AMSTERDAM"),
    BERLIN("BER", "BERLIN"),
    LONDON("LON", "LONDON"),
    ROMA("ROM", "ROMA"),
    TIRANA("TIA", "TIRANA"),
    VIENNA("VIE", "VIENNA"),
    BRUSSELS("BRU", "BRUSSELS"),
    SPLIT("SPU", "SPLIT"),
    ZAGRZEB("ZAG", "ZAGRZEB");


    private String airportCode;
    private String cityName;

    City(String city, String cityName) {
        this.airportCode = city;
        this.cityName = cityName;
    }
}
