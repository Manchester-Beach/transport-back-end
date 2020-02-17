package com.manchesterbeach.transport.service;

import com.google.gson.Gson;
import com.manchesterbeach.transport.domain.TramDeparture;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeparturesService {
    @Autowired
    private RestTemplate restTemplate;
    public ResponseEntity getTramDepartures(String stationIdentifier, int timeOffset) throws ParseException {
        String URL = "https://www.tramchester.com/api/departures/station/" + stationIdentifier;
        ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);
        if (response.getStatusCode() != HttpStatus.OK) return null;
        return formatResponseBody(response.getBody());
    }
    public ResponseEntity formatResponseBody(String responseBody) throws ParseException {
        Gson gson = new Gson();
        List<TramDeparture> tramDepartures = new ArrayList<TramDeparture>();
        JSONObject responseBodyJSONObject = (JSONObject)new JSONParser().parse(responseBody);
        JSONArray departures = (JSONArray)responseBodyJSONObject.get("departures");
        for (int index = 0; index < departures.size(); index++) {
            JSONObject departure = (JSONObject)departures.get(index);
            if ((long)departure.get("wait") >= 5) tramDepartures.add(new TramDeparture((long)departure.get("wait"), (String)departure.get("destination")));
            if(tramDepartures.size() == 3) { return new ResponseEntity(gson.toJson(tramDepartures), HttpStatus.OK); }
        }
        return new ResponseEntity(gson.toJson(tramDepartures), HttpStatus.OK);
    }
}
