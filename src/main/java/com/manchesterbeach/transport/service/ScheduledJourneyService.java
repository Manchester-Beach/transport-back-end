package com.manchesterbeach.transport.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.manchesterbeach.transport.domain.ScheduledJourney;
import com.manchesterbeach.transport.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScheduledJourneyService {

    @Autowired
    RestTemplate restTemplate;

    public ScheduledJourney getJourneyDetails(Station departureStation, Station arrivalStation) {
        String url = String.format("https://trains.mcrlab.co.uk/next/%s/%s", departureStation.getId(), arrivalStation.getId());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return jsonResponseAsJourney(response.getBody());
    }

    public ScheduledJourney jsonResponseAsJourney(String json){
        Gson g = new Gson();

        JsonElement jelement = g.fromJson(json, JsonElement.class);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("departures");

        if(jarray.size() <= 0){
            return null;
        }

        jobject = jarray.get(0).getAsJsonObject();

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
}
