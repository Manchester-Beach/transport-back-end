package com.manchesterbeach.transport.domain;

import lombok.Data;

@Data
public class Journey {

    private final String name;
    private final Station originStation;
    private final Station destinationStation;

}
