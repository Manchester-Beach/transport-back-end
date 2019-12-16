package com.manchesterbeach.transport.controller;

import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping(value = "/stations")
    public ResponseEntity<Map<String, List<Station>>> getAllStations(){
        List<Station> response = stationService.getAllStations();
        Map<String, List<Station>> jsonResponseBody = new HashMap<>();

        if(response == null){
            return new ResponseEntity<>(jsonResponseBody, HttpStatus.NO_CONTENT);
        }

        jsonResponseBody.put("stations", response);

        return new ResponseEntity<>(jsonResponseBody, HttpStatus.OK);
    }

    @GetMapping(value = "/stations/{id}")
    public ResponseEntity<Map<String, Object>> getStationFromId(@PathVariable("id") String id){
        Station response = stationService.getOneStation(id);
        Map<String, Object> responseBody = new HashMap<String, Object>();

        if(response == null){
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        responseBody.put("station", response);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
