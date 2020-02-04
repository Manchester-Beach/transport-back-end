package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.ScheduledJourney;
import com.manchesterbeach.transport.domain.Station;
import jdk.nashorn.internal.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        doReturn(scheduledJourney).when(mockService).jsonResponseAsJourney(anyString(), anyInt());

        //when
        ScheduledJourney response = mockService.getJourneyDetails(originStation, destinationStation, 0);

        //then
        assertThat(response).isEqualTo(scheduledJourney);
    }

    @Test
    public void shouldConvertJSONResponseToJourney() throws IOException {
        String JSON = new String(Files.readAllBytes(Paths.get("./src/test/mocks/ScheduledJourneyResponse.json")));
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "", "15:38", "-1:58", "16:26", true);
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.jsonResponseAsJourney(JSON, 0);
        assertThat(actualScheduledJourney).isEqualTo(expectedScheduledJourney);
    }

    @Test
    public void shouldReturnSecondDepartureWhenRequested() throws IOException {
        String JSON = new String(Files.readAllBytes(Paths.get("./src/test/mocks/ScheduledJourneyResponse.json")));
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "2", "16:36", "16:36", "17:24", false);
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.jsonResponseAsJourney(JSON, 1);
        assertThat(actualScheduledJourney).isEqualTo(expectedScheduledJourney);
    }

    @Test
    public void shouldReturnNullForInvalidJSONInRequest() {
        String JSON = "I am invalid!";
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.jsonResponseAsJourney(JSON, 0);
        assertThat(actualScheduledJourney).isEqualTo(null);
    }

    @Test
    public void shouldDefaultToFirstDepartureIfJourneyIndexNotSpecified() throws IOException {
        String JSON = new String(Files.readAllBytes(Paths.get("./src/test/mocks/ScheduledJourneyResponse.json")));
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "", "15:38", "-1:58", "16:26", true);
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.jsonResponseAsJourney(JSON);
        assertThat(actualScheduledJourney).isEqualTo(expectedScheduledJourney);
    }

}