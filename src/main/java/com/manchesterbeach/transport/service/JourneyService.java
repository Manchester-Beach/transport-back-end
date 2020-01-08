package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.Journey;
import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.repo.CrudRepo;
import com.manchesterbeach.transport.repo.JourneyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class JourneyService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CrudRepo<Journey> journeyRepo;

    @Autowired
    private StationService stationService;

    public ResponseEntity saveJourney(String originCrs, String destinationCrs) throws URISyntaxException {
        URI uri = new URI("/journeys");

        Station originStation = stationService.getOneStation(originCrs);
        Station destinationStation = stationService.getOneStation(destinationCrs);

        if(originStation == null || destinationStation == null){
            return ResponseEntity.badRequest().build();
        }

        journeyRepo.save(new Journey(originStation, destinationStation));
        System.out.println("Journey added, count is now: " + journeyRepo.count());

        return ResponseEntity.created(uri).build();
    }

    public List<Journey> getAllJourneys() {
        return journeyRepo.findAll();
    }

    public ResponseEntity deleteJourney(int journeyIndex) {
        Journey journeyToDelete = journeyRepo.findById(Long.valueOf(journeyIndex));

        if(journeyToDelete == null){
            return ResponseEntity.badRequest().build();
        }

        return journeyRepo.delete(journeyToDelete);

    }
}
