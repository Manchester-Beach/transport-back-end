package com.manchesterbeach.transport.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.manchesterbeach.transport.domain.ScheduledJourney;
import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.utils.EmptyJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScheduledJourneyService {

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity getJourneyDetails(Station departureStation, Station arrivalStation) {
        return this.getJourneyDetails(departureStation, arrivalStation, 0);
    }

    public ResponseEntity getJourneyDetails(Station departureStation, Station arrivalStation, int journeyIndex) {

        if(departureStation == null || arrivalStation == null) {
            return new ResponseEntity("Request Error! Please report to Team Beach.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ScheduledJourney scheduledJourney;
        String url = String.format("https://trains.mcrlab.co.uk/next/%s/%s", departureStation.getId(), arrivalStation.getId());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("There's a problem: " + response.getStatusCode());
            return new ResponseEntity("Internal server error! Please report to Team Beach.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //journey index is the (n - 1)th journey to retrieve,
        scheduledJourney = jsonResponseAsJourney(response.getBody(), journeyIndex);

        if(scheduledJourney == null)
        {
            return new ResponseEntity("No direct trains available!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(scheduledJourney, HttpStatus.OK);
    }

    public ScheduledJourney jsonResponseAsJourney(String json) {
        return this.jsonResponseAsJourney(json, 0);
    }

    public ScheduledJourney jsonResponseAsJourney(String json, int journeyIndex){
        try {
            Gson g = new Gson();

            JsonElement jelement = g.fromJson(json, JsonElement.class);
            JsonObject jobject = jelement.getAsJsonObject();
            JsonArray jarray = jobject.getAsJsonArray("departures");

            if(jarray.size() <= 0){
                return null;
            }

            if(journeyIndex >= jarray.size()){
                return null;
            }

            jobject = jarray.get(journeyIndex).getAsJsonObject();

            JsonObject originStation = jobject.getAsJsonObject("origin");
            String originStn = originStation.get("name").getAsString();
            String originStnCrs = originStation.get("crs").getAsString();
            String intendedTime = originStation.get("scheduled").getAsString();
            String estimatedTime = originStation.get("estimated").getAsString();

            JsonObject destStation = jobject.getAsJsonObject("destination");
            String destStn = destStation.get("name").getAsString();
            String destStnCrs = destStation.get("crs").getAsString();
            String estimatedArrival = destStation.get("scheduled").getAsString();
            String platformNo = jobject.get("platform").getAsString();
            Boolean cancelled = jobject.get("isCancelled").getAsBoolean();

            Station origin = new Station(originStnCrs, originStn, 0, 0);
            Station destination = new Station(destStnCrs, destStn, 0, 0);

            return new ScheduledJourney(origin, destination, platformNo, intendedTime, estimatedTime, estimatedArrival, cancelled);
        }
        catch(Exception exception) {
            return null;
        }
    }

    public ResponseEntity getAllJourneyDetails(Station departureStation, Station arrivalStation) {

        if(departureStation == null || arrivalStation == null) {
            return new ResponseEntity("Request Error! Please report to Team Beach.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ScheduledJourney scheduledJourney;
        String url = String.format("https://trains.mcrlab.co.uk/next/%s/%s", departureStation.getId(), arrivalStation.getId());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("There's a problem: " + response.getStatusCode());
            return new ResponseEntity("Internal server error! Please report to Team Beach.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson g = new Gson();
        JsonElement jelement = g.fromJson(response.getBody(), JsonElement.class);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("departures");

        if (jarray.size() == 0){
            return new ResponseEntity("No direct trains available!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ScheduledJourney[] returnArray = new ScheduledJourney[jarray.size()];

        for(int i = 0; i < jarray.size(); i++){
            returnArray[i] = jsonResponseAsJourney(response.getBody(), i);
        }

        return new ResponseEntity<>(returnArray, HttpStatus.OK);
    }
}
