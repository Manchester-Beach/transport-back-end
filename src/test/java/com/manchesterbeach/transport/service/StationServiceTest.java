package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.Station;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

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

}