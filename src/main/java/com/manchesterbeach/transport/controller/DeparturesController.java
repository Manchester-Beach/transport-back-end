package com.manchesterbeach.transport.controller;

import com.manchesterbeach.transport.service.DeparturesService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://beach-train-app.herokuapp.com", "https://beach-train-app.herokuapp.com", "http://beach-train-app-qa.herokuapp.com", "https://beach-train-app-qa.herokuapp.com"})
@RestController
public class DeparturesController {

    @Autowired
    private DeparturesService departuresService;

    @GetMapping(produces = "application/json", value = "/departures/tram/{stationIdentifier}")
    public ResponseEntity getTramDepartures(@PathVariable String stationIdentifier, @RequestParam(required = false) Integer timeOffset) throws ParseException {
         if (timeOffset != null) {
            return departuresService.getTramDepartures(stationIdentifier, timeOffset.intValue());
        }
        return departuresService.getTramDepartures(stationIdentifier, 5);
    }
}
