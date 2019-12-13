package com.manchesterbeach.transport.controller;

import com.manchesterbeach.transport.domain.Journey;
import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class JourneyController {

    @Autowired
    private JourneyService journeyService;

    @PostMapping(value = "/journeys", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addJourney(@RequestBody Journey journey) throws URISyntaxException {
        ResponseEntity response = journeyService.saveJourney(journey.getOriginStation(), journey.getDestinationStation());
        URI uri = new URI("/journeys");

        return response.getStatusCode() == HttpStatus.CREATED ?
                ResponseEntity.created(uri).build() : ResponseEntity.badRequest().build();

    }
}
