package com.manchesterbeach.transport.domain;

import java.util.Objects;

public class TramDeparture {
    private long timeUntilDeparture;
    private String destination;
    public TramDeparture(long timeUntilDeparture, String destination) {
        this.timeUntilDeparture =timeUntilDeparture;
        this.destination = destination;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || this.getClass() != object.getClass()) return false;
        TramDeparture tramDeparture = (TramDeparture)object;
        return Objects.equals(this.timeUntilDeparture, tramDeparture.timeUntilDeparture) && Objects.equals(this.destination, tramDeparture.destination);
    }
}
