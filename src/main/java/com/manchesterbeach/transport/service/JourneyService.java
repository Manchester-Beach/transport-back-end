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

    private CrudRepo<Journey> journeyRepo;

    private StationService stationService;

    JourneyService(StationService stationService, CrudRepo<Journey> journeyRepo) {
        this.stationService = stationService;
        this.journeyRepo = journeyRepo;
    }

    public ResponseEntity saveJourney(String originCrs, String destinationCrs) throws URISyntaxException {
        URI uri = new URI("/journeys");

        Station originStation = stationService.getOneStation(originCrs);
        Station destinationStation = stationService.getOneStation(destinationCrs);

        if(originStation == null || destinationStation == null){
            return ResponseEntity.badRequest().build();
        }

        if(journeyRepo.findAll().contains(new Journey(originStation, destinationStation))) {
            return ResponseEntity.ok().build();
        }
        else {
            journeyRepo.save(new Journey(originStation, destinationStation));
            System.out.println("Journey added, count is now: " + journeyRepo.count());
        }

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
