package com.manchesterbeach.transport.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ScheduledJourney {

    private Station originStation;
    private Station destinationStation;
    private String platform;
    private String scheduledDeparture;
    private String expectedDeparture;
    private String arrivalTime;
    private String expectedArrivalTime;
    private Boolean cancelled;

    public ScheduledJourney(Station originStation, Station destinationStation, String platform, String scheduledDeparture, String expectedDeparture, String arrivalTime, String expectedArrivalTime, Boolean cancelled) {
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.platform = platform;
        this.scheduledDeparture = scheduledDeparture;
        this.expectedDeparture = expectedDeparture;
        this.arrivalTime = arrivalTime;
        this.expectedArrivalTime = expectedArrivalTime;
        this.cancelled = cancelled;
    }

    public Station getOriginStation() {
        return originStation;
    }

    public void setOriginStation(Station originStation) {
        this.originStation = originStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(Station destinationStation) {
        this.destinationStation = destinationStation;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(String scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    public String getExpectedDeparture() {
        return expectedDeparture;
    }

    public void setExpectedDeparture(String expectedDeparture) {
        this.expectedDeparture = expectedDeparture;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public void setExpectedArrivalTime(String expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
}
