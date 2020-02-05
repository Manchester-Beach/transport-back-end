package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.Station;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StationService stationService;

    @Test
    public void shouldReturnAllStations(){
        //given
        String uri = "https://trains.mcrlab.co.uk/stations";
        Station[] expectedStations = new Station[]{
            new Station("123", "Manchester Piccadilly", 1, 1),
            new Station("124", "Liverpool Lime Street", 1, 1)
        };

        ResponseEntity<Station[]> expectedResponse = new ResponseEntity<>(expectedStations, HttpStatus.OK);

        doReturn(expectedResponse).when(restTemplate).getForEntity(uri, Station[].class);

        //when
        List<Station> response = stationService.getAllStations();

        //then
        assertThat(response).isEqualTo(Arrays.asList(expectedStations));
    }

    @Test
    public void shouldReturnCorrectStation() {
        Station[] mockResponseStations = new Station[]{
                new Station("MCV", "Manchester Victoria", 0, 0),
                new Station("124", "Liverpool Lime Street", 1, 1)
        };
        Station expectedStation = new Station("MCV", "Manchester Victoria", 0 ,0);
        when(restTemplate.getForEntity("https://trains.mcrlab.co.uk/stations", Station[].class)).thenReturn(new ResponseEntity(mockResponseStations, HttpStatus.OK));
        Station actualStation = stationService.getOneStation("MCV");
        assertThat(actualStation).isEqualTo(expectedStation);
    }

    @Test
    public void shouldReturnNullIfStationDoesNotExist() {
        Station[] mockResponseStations = new Station[]{
                new Station("MCV", "Manchester Victoria", 0, 0),
                new Station("124", "Liverpool Lime Street", 1, 1)
        };
        when(restTemplate.getForEntity("https://trains.mcrlab.co.uk/stations", Station[].class)).thenReturn(new ResponseEntity(mockResponseStations, HttpStatus.OK));
        Station actualStation = stationService.getOneStation("DAN");
        assertThat(actualStation).isEqualTo(null);
    }

    @Test
    public void shouldReturnNullIfNoStationSpecified() {
        Station[] mockResponseStations = new Station[]{
                new Station("MCV", "Manchester Victoria", 0, 0),
                new Station("124", "Liverpool Lime Street", 1, 1)
        };
        when(restTemplate.getForEntity("https://trains.mcrlab.co.uk/stations", Station[].class)).thenReturn(new ResponseEntity(mockResponseStations, HttpStatus.OK));
        Station actualStation = stationService.getOneStation("");
        assertThat(actualStation).isEqualTo(null);
        actualStation = stationService.getOneStation(null);
        assertThat(actualStation).isEqualTo(null);
    }

}