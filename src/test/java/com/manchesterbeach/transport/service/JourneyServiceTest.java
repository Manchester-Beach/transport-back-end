package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.Journey;
import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.repo.CrudRepo;
import com.manchesterbeach.transport.repo.JourneyRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
                         import com.manchesterbeach.transport.domain.Journey;
@SpringBootTest
public class JourneyServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private JourneyRepo journeyRepo;


    @Autowired
    private JourneyService journeyService;

    @Mock
    private StationService stationService;

    @Test
    public void shouldGetAllJourneys(){
        //given
        String uri = "/journeys";

        //when

        //then
    }

    @Test
    public void shouldDeleteJourney(){
        //given
        journeyRepo = mock(JourneyRepo.class);
        journeyService = new JourneyService(stationService, journeyRepo);
        int journeyIndex = 0;
        Journey journeyToDelete = new Journey();

        when(journeyRepo.findById(Long.valueOf(journeyIndex))).thenReturn(journeyToDelete);
        when(journeyRepo.delete(journeyToDelete)).thenReturn(ResponseEntity.ok().build());

        //when
        ResponseEntity response = journeyService.deleteJourney(journeyIndex);

        //then
        verify(journeyRepo).delete(journeyToDelete);
    }

    @Test
    public void shouldAddJourneyIfJourneyDoesNotAlreadyExist() throws URISyntaxException {
        // Given
        stationService = mock(StationService.class);
        journeyRepo = mock(JourneyRepo.class);

        String originCRS = "MCV";
        Station originStation = new Station(originCRS, "Manchester Victoria", 0, 0);
        String destinationCRS = "LDS";
        Station destinationStation = new Station(destinationCRS, "Leeds", 0, 0);

        when(stationService.getOneStation(originCRS)).thenReturn(originStation);
        when(stationService.getOneStation(destinationCRS)).thenReturn(destinationStation);

        // When
        journeyService = new JourneyService(stationService, journeyRepo);
        journeyService.saveJourney(originCRS, destinationCRS);
        // Then
        verify(journeyRepo).save(new Journey(originStation, destinationStation));
    }

    @Test
    public void shouldNotAddJourneyIfJourneyAlreadyExists() throws URISyntaxException {
        // Given
        stationService = mock(StationService.class);
        journeyRepo = mock(JourneyRepo.class);

        String originCRS = "MCV";
        Station originStation = new Station(originCRS, "Manchester Victoria", 0, 0);
        String destinationCRS = "LDS";
        Station destinationStation = new Station(destinationCRS, "Leeds", 0, 0);

        when(stationService.getOneStation(originCRS)).thenReturn(originStation);
        when(stationService.getOneStation(destinationCRS)).thenReturn(destinationStation);
        when(journeyRepo.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new Journey(originStation, destinationStation))));

        // When
        journeyService = new JourneyService(stationService, journeyRepo);
        ResponseEntity responseEntity = journeyService.saveJourney(originCRS, destinationCRS);
        // Then
        verify(journeyRepo, times(0)).save(new Journey(originStation, destinationStation));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
