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
    public void shouldGetScheduledJourneyDetails() throws IOException {
        String JSON = new String(Files.readAllBytes(Paths.get("./src/test/mocks/ScheduledJourneyResponse.json")));
        when(restTemplate.getForEntity("https://trains.mcrlab.co.uk/next/BYM/MCV", String.class)).thenReturn(new ResponseEntity<>(JSON, HttpStatus.OK));
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "", "15:38", "-1:58", "16:26", true);
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.getJourneyDetails(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), 0);
        assertThat(actualScheduledJourney).isEqualTo(expectedScheduledJourney);
    }

    @Test
    public void shouldReturnFirstDepartureIfJourneyIndexNotSpecified() throws IOException {
        String JSON = new String(Files.readAllBytes(Paths.get("./src/test/mocks/ScheduledJourneyResponse.json")));
        when(restTemplate.getForEntity("https://trains.mcrlab.co.uk/next/BYM/MCV", String.class)).thenReturn(new ResponseEntity<>(JSON, HttpStatus.OK));
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "", "15:38", "-1:58", "16:26", true);
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.getJourneyDetails(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0));
        assertThat(actualScheduledJourney).isEqualTo(expectedScheduledJourney);
    }

    @Test
    public void shouldReturnNullForServiceError() {
        when(restTemplate.getForEntity("https://trains.mcrlab.co.uk/next/BYM/MCV", String.class)).thenReturn(new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR));
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.getJourneyDetails(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), 0);
        assertThat(actualScheduledJourney).isEqualTo(null);
    }

    @Test
    public void shouldReturnNullForNullStationParameter() {
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.getJourneyDetails(new Station("BYM", "Burnley Manchester Road", 0, 0), null, 0);
        assertThat(actualScheduledJourney).isEqualTo(null);
        actualScheduledJourney = scheduledJourneyService.getJourneyDetails(null, new Station("BYM", "Burnley Manchester Road", 0, 0), 0);
        assertThat(actualScheduledJourney).isEqualTo(null);
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