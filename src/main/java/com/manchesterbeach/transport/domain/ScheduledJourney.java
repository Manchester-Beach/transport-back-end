package com.manchesterbeach.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ScheduledJourney extends Journey {

    private String scheduledDeparture;
    private String expectedDeparture;
    private String platform;
    private String arrivalTime;

    public ScheduledJourney(Station departureStation, Station arrivalStation, String scheduledDeparture, String expectedDeparture, String platform, String arrivalTime) {
        this.setOriginStation(departureStation);
        this.setDestinationStation(arrivalStation);
        this.scheduledDeparture = scheduledDeparture;
        this.expectedDeparture = expectedDeparture;
        this.platform = platform;
        this.arrivalTime = arrivalTime;
    }
}
