package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    @Autowired
    private RestTemplate restTemplate;

    public List<Station> getAllStations() {
        String uri = "https://trains.mcrlab.co.uk/stations";

        ResponseEntity<Station[]> response = restTemplate.getForEntity(uri, Station[].class);

        return response.getStatusCode() == HttpStatus.OK ? Arrays.asList(response.getBody()) : null;
    }

    public Station getOneStation(String id) {
        String uri = "https://trains.mcrlab.co.uk/stations";

        ResponseEntity<Station[]> response = restTemplate.getForEntity(uri, Station[].class);

        List<Station> allStations = Arrays.asList(response.getBody());

        allStations = allStations.stream().filter(oneStation -> oneStation.getId().equals(id)).collect(Collectors.toList());


        if(allStations.size() < 1){
            return null;
        }

        return response.getStatusCode() == HttpStatus.OK ? allStations.get(0) : null;
    }
}
