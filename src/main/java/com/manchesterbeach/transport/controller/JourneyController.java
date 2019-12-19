package com.manchesterbeach.transport.controller;

import com.manchesterbeach.transport.domain.Journey;
import com.manchesterbeach.transport.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:3000")
@RestController
public class JourneyController {

    @Autowired
    private JourneyService journeyService;

    @PostMapping(value = "/journeys", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addJourney(@RequestBody Map<String, String> json) throws URISyntaxException {
        ResponseEntity response = journeyService.saveJourney(json.get("origin"), json.get("destination"));
        URI uri = new URI("/journeys");

        return response.getStatusCode() == HttpStatus.CREATED ?
                ResponseEntity.created(uri).build() : ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/journeys", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Journey>>> getAllJourneys() {
        List<Journey> response = journeyService.getAllJourneys();

        Map<String, List<Journey>> jsonResponseBody = new HashMap<>();

        if(response == null){
            return new ResponseEntity<>(jsonResponseBody, HttpStatus.NO_CONTENT);
        }

        jsonResponseBody.put("journeys", response);

        return new ResponseEntity<>(jsonResponseBody, HttpStatus.OK);
    }
}
