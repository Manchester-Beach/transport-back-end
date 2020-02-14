package com.manchesterbeach.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manchesterbeach.transport.domain.ScheduledJourney;
import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.service.ScheduledJourneyService;
import com.manchesterbeach.transport.service.StationService;
import com.manchesterbeach.transport.utils.EmptyJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:3000", "http://beach-train-app.herokuapp.com", "https://beach-train-app.herokuapp.com", "http://beach-train-app-qa.herokuapp.com", "https://beach-train-app-qa.herokuapp.com"})
@RestController
public class ScheduledJourneyController {

    @Autowired
    private ScheduledJourneyService scheduledJourneyService;

    @Autowired
    private StationService stationService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping(value = "/scheduledJourneys/{origin}/{destination}")
    public ResponseEntity getAllScheduledJourneys(@PathVariable("origin") String origin, @PathVariable("destination") String destination) {
        Station originStation = stationService.getOneStation(origin);
        Station destinationStation = stationService.getOneStation(destination);

        ResponseEntity scheduledJourneys = scheduledJourneyService.getAllJourneyDetails(originStation, destinationStation);

        return scheduledJourneys;
    }

    @GetMapping(value="/scheduledJourneys/{origin}/{destination}/{index}")
    public ResponseEntity getScheduledJourney(@PathVariable("origin") String origin,
                                              @PathVariable("destination") String destination,
                                              @PathVariable("index") int journeyIndex) {
        Station originStation = stationService.getOneStation(origin);
        Station destinationStation = stationService.getOneStation(destination);

        ResponseEntity scheduledJourney = scheduledJourneyService.getJourneyDetails(originStation, destinationStation, journeyIndex);

        return scheduledJourney;
    }
}
