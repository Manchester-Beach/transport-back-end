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

@CrossOrigin(origins = {"http://localhost:3000", "http://beach-train-app.herokuapp.com", "https://beach-train-app.herokuapp.com", "http://beach-train-app-qa.herokuapp.com", "https://beach-train-app-qa.herokuapp.com"})
@RestController
public class JourneyController {

    @Autowired
    private JourneyService journeyService;

    @PostMapping(value = "/journeys", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addJourney(@RequestBody Map<String, String> json) throws URISyntaxException {
        return journeyService.saveJourney(json.get("origin"), json.get("destination"));
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

    @CrossOrigin(origins = {"http://localhost:3000", "http://beach-train-app.herokuapp.com"})
    @DeleteMapping(value = "/journeys/{index}")
    public ResponseEntity<Object> deleteJourney(@PathVariable int index){
        ResponseEntity response = journeyService.deleteJourney(index);

        return response.getStatusCode() == HttpStatus.OK ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build() ;

    }
}
