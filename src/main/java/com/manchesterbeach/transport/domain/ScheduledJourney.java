package com.manchesterbeach.transport.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduledJourney {

    private Station originStation;
    private Station destinationStation;
    private String platform;
    private String scheduledDeparture;
    private String expectedDeparture;
    private String arrivalTime;
    private Boolean cancelled;
}
