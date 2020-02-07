package com.manchesterbeach.transport.service;

import com.google.gson.Gson;
import com.manchesterbeach.transport.domain.TramDeparture;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DeparturesServiceTest {

    @InjectMocks
    private DeparturesService departuresService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void shouldRequestDeparturesFromCorrectEndpoint() throws ParseException {
        String responseBody = "{\"departures\":[{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Bury\",\"when\":\"12:44\",\"wait\":7},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Piccadilly\",\"when\":\"12:44\",\"wait\":7},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Victoria\",\"when\":\"12:46\",\"wait\":9},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Manchester Airport\",\"when\":\"12:47\",\"wait\":10}],\"notes\":[\"'On Sunday 9th February Manchester City welcome West Ham United to the Etihad Stadium. Kick Off is 16:30hrs and services are expected to be busier than usual. Please allow extra time for your journey.' - Shudehill, Metrolink\"]}";
        String stationIdentifier = "9400ZZMASHU";
        String URL = "https://www.tramchester.com/api/departures/station/" + stationIdentifier;
        when(restTemplate.getForEntity(URL, String.class)).thenReturn(new ResponseEntity(responseBody, HttpStatus.OK));
        departuresService.getTramDepartures(stationIdentifier);
        verify(restTemplate, times(1)).getForEntity(URL, String.class);
    }
    @Test
    public void shouldReturnNullForResponseThatIsNotOk() throws ParseException {
        String stationIdentifier = "9400ZZMASHU";
        String URL = "https://www.tramchester.com/api/departures/station/" + stationIdentifier;
        when(restTemplate.getForEntity(URL, String.class)).thenReturn(new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR));
        departuresService.getTramDepartures(stationIdentifier);
        verify(restTemplate, times(1)).getForEntity(URL, String.class);
    }
    @Test
    public void shouldReturnThreeDepartures() throws ParseException {
        Gson gson = new Gson();
        String responseBody = "{\"departures\":[{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Bury\",\"when\":\"12:44\",\"wait\":7},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Piccadilly\",\"when\":\"12:44\",\"wait\":7},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Victoria\",\"when\":\"12:46\",\"wait\":9},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Manchester Airport\",\"when\":\"12:47\",\"wait\":10}],\"notes\":[\"'On Sunday 9th February Manchester City welcome West Ham United to the Etihad Stadium. Kick Off is 16:30hrs and services are expected to be busier than usual. Please allow extra time for your journey.' - Shudehill, Metrolink\"]}";
        List<TramDeparture> expectedTramDeparturesList = new ArrayList<TramDeparture>();
        expectedTramDeparturesList.add(new TramDeparture(7, "Bury"));
        expectedTramDeparturesList.add(new TramDeparture(7, "Piccadilly"));
        expectedTramDeparturesList.add(new TramDeparture(9, "Victoria"));
        ResponseEntity expectedTramDepartures = new ResponseEntity(gson.toJson(expectedTramDeparturesList), HttpStatus.OK);
        ResponseEntity actualTramDepartures = departuresService.formatResponseBody(responseBody);
        assertThat(actualTramDepartures.getBody()).isEqualTo(expectedTramDepartures.getBody());
    }
    @Test
    public void shouldNotReturnDeparturesWhichDepartInLessThanFiveMinutes() throws ParseException {
        Gson gson = new Gson();
        String responseBody = "{\"departures\":[{\"from\":\"Shudehill\",\"carriages\":\"Double\",\"status\":\"Due\",\"destination\":\"Altrincham\",\"when\":\"12:39\",\"wait\":2},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Bury\",\"when\":\"12:44\",\"wait\":7},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Piccadilly\",\"when\":\"12:44\",\"wait\":7},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Victoria\",\"when\":\"12:46\",\"wait\":9},{\"from\":\"Shudehill\",\"carriages\":\"Single\",\"status\":\"Due\",\"destination\":\"Manchester Airport\",\"when\":\"12:47\",\"wait\":10}],\"notes\":[\"'On Sunday 9th February Manchester City welcome West Ham United to the Etihad Stadium. Kick Off is 16:30hrs and services are expected to be busier than usual. Please allow extra time for your journey.' - Shudehill, Metrolink\"]}";
        List<TramDeparture> expectedTramDeparturesList = new ArrayList<TramDeparture>();
        expectedTramDeparturesList.add(new TramDeparture(7, "Bury"));
        expectedTramDeparturesList.add(new TramDeparture(7, "Piccadilly"));
        expectedTramDeparturesList.add(new TramDeparture(9, "Victoria"));
        ResponseEntity expectedTramDepartures = new ResponseEntity(gson.toJson(expectedTramDeparturesList), HttpStatus.OK);
        ResponseEntity actualTramDepartures = departuresService.formatResponseBody(responseBody);
        assertThat(actualTramDepartures.getBody()).isEqualTo(expectedTramDepartures.getBody());
    }
}
