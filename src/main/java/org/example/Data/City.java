package org.example.Data;


import lombok.Getter;

@Getter
public enum City {
    //http://www.lotnictwocywilne.pl/kodylotnisk.html -> code of airport
    WARSAW("WAW", "WARSAW", "Warszawa"),
    PARIS("PAR", "PARIS", "Paryż"),
    MALLORCA("PMI", "MALLORCA", "Palma%20de%20Mallorca"),
    PAFOS("PFO", "PAFOS", "Pafos"),
    BARCELONA("BCN", "BARCELONA", "Barcelona"),
    AMSTERDAM("AMS", "AMSTERDAM", "Amsterdam"),
    BERLIN("BER", "BERLIN", "Berlin"),
    LONDON("LON", "LONDON", "Londyn"),
    ROMA("ROM", "ROMA", "Rzym"),
    TIRANA("TIA", "TIRANA", "Tirana"),
    VIENNA("VIE", "VIENNA", "Wiedeń"),
    BRUSSELS("BRU", "BRUSSELS", "Bruksela"),
    SPLIT("SPU", "SPLIT", "Split"),
    ZAGRZEB("ZAG", "ZAGRZEB", "Zagrzeb"),
    PRAGA("PRG", "PRAGA", "Praga");


    private String airportCode;
    private String cityName;
    private String polishName;

    City(String airportCode, String cityName, String polishName) {
        this.airportCode = airportCode;
        this.cityName = cityName;
        this.polishName = polishName;
    }
}
