package com.manchesterbeach.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manchesterbeach.transport.domain.ScheduledJourney;
import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.service.ScheduledJourneyService;
import com.manchesterbeach.transport.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduledJourneyController {

    @Autowired
    private ScheduledJourneyService scheduledJourneyService;

    @Autowired
    private StationService stationService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping(value="/scheduledJourneys/{origin}/{destination}")
    public ResponseEntity<ScheduledJourney> getScheduledJourney(@PathVariable("origin") String origin, @PathVariable("destination") String destination) {
        Station originStation = stationService.getOneStation(origin);
        Station destinationStation = stationService.getOneStation(destination);

        ScheduledJourney scheduledJourney = scheduledJourneyService.getJourneyDetails(originStation, destinationStation);
        if (scheduledJourney == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(scheduledJourney, HttpStatus.OK);
    }
}
