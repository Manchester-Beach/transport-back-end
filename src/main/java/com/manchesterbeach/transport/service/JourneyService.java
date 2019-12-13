package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.Journey;
import com.manchesterbeach.transport.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JourneyService {

    @Autowired
    private RestTemplate restTemplate;

    private List<Journey> journeyList = new ArrayList();

    public ResponseEntity saveJourney(Station originStation, Station destinationStation) throws URISyntaxException {
        URI uri = new URI("/journeys");

        journeyList.add(new Journey(originStation, destinationStation));
        System.out.println("Journey added: #" + journeyList.size());

        return ResponseEntity.created(uri).build();
    }
}
