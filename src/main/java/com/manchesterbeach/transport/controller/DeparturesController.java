package com.manchesterbeach.transport.controller;

import com.manchesterbeach.transport.service.DeparturesService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:3000", "http://beach-train-app.herokuapp.com", "https://beach-train-app.herokuapp.com", "http://beach-train-app-qa.herokuapp.com", "https://beach-train-app-qa.herokuapp.com"})
@RestController
public class DeparturesController {

    @Autowired
    private DeparturesService departuresService;

    @GetMapping(produces = "application/json", value = "/departures/tram/{stationIdentifier}")
    public ResponseEntity getTramDepartures(@PathVariable String stationIdentifier) throws ParseException {
        return departuresService.getTramDepartures(stationIdentifier);
    }
}
