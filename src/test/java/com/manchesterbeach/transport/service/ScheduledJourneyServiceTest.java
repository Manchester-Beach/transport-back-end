package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.ScheduledJourney;
import com.manchesterbeach.transport.domain.Station;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScheduledJourneyServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ScheduledJourneyService scheduledJourneyService;

    @Test
    public void shouldGetScheduledJourneyDetailsFromStations() {
        //given
        ScheduledJourneyService mockService = spy(scheduledJourneyService);
        Station originStation = new Station("LIV", "Liverpool Lime Street", 0, 0);
        Station destinationStation = new Station("MCV", "Manchester Victoria", 0, 0);
        String url = String.format("https://trains.mcrlab.co.uk/next/%s/%s", originStation.getId(), destinationStation.getId());
        ScheduledJourney scheduledJourney = new ScheduledJourney(originStation, destinationStation, "5", "17:04", "17:24", "18:00", false);
        when(restTemplate.getForEntity(url, String.class)).thenReturn(new ResponseEntity<>("", HttpStatus.OK));
        doReturn(scheduledJourney).when(mockService).jsonResponseAsJourney(anyString());

        //when
        ScheduledJourney response = mockService.getJourneyDetails(originStation, destinationStation);

        //then
        assertThat(response).isEqualTo(scheduledJourney);
    }

}