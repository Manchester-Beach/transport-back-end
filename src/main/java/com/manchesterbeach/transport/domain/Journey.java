package com.manchesterbeach.transport.domain;

import lombok.Data;

@Data
public class Journey {

    private String name;
    private Station originStation;
    private Station destinationStation;

    public Journey(String name, Station originStation, Station destinationStation) {
        this.name = name;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
    }
}
