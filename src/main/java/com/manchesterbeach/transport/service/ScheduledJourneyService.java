package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.ScheduledJourney;
import com.manchesterbeach.transport.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class ScheduledJourneyService {

    public ScheduledJourney getJourneyDetails(Station departureStation, Station arrivalStation) {
        return new ScheduledJourney(new Station("a", "ahh"), new Station("a", "ahh"), "","","","");
    }
}
