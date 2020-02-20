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
        when(restTemplate.getForEntity("https://beach-train-ldb.herokuapp.com/journeys/BYM/MCV", String.class)).thenReturn(new ResponseEntity<>(JSON, HttpStatus.OK));
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "", "15:38", "-1:58", "16:26", "16:26", true);
        ScheduledJourney actualScheduledJourney = (ScheduledJourney)scheduledJourneyService.getJourneyDetails(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), 0).getBody();
        assertThat(actualScheduledJourney).isEqualTo(expectedScheduledJourney);
    }

    @Test
    public void shouldReturnFirstDepartureIfJourneyIndexNotSpecified() throws IOException {
        String JSON = new String(Files.readAllBytes(Paths.get("./src/test/mocks/ScheduledJourneyResponse.json")));
        when(restTemplate.getForEntity("https://beach-train-ldb.herokuapp.com/journeys/BYM/MCV", String.class)).thenReturn(new ResponseEntity<>(JSON, HttpStatus.OK));
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "", "15:38", "-1:58", "16:26", "16:26", true);
        ScheduledJourney actualScheduledJourney = (ScheduledJourney)scheduledJourneyService.getJourneyDetails(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0)).getBody();
        assertThat(actualScheduledJourney).isEqualTo(expectedScheduledJourney);
    }

    @Test
    public void shouldReturnMessageForServiceError() {
        when(restTemplate.getForEntity("https://beach-train-ldb.herokuapp.com/journeys/BYM/MCV", String.class)).thenReturn(new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR));
        String errorMessage = (String)scheduledJourneyService.getJourneyDetails(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), 0).getBody();
        assertThat(errorMessage).isEqualTo("Internal server error! Please report to Team Beach.");
    }

    @Test void shouldReturnMessageForNoJourneys() {
        when(restTemplate.getForEntity("https://beach-train-ldb.herokuapp.com/journeys/BYM/MCV", String.class)).thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        String errorMessage = (String)scheduledJourneyService.getJourneyDetails(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), 0).getBody();
        assertThat(errorMessage).isEqualTo("No direct trains available!");
    }

    @Test
    public void shouldReturnMessageForNullStationParameter() {
        String errorMessage = (String) scheduledJourneyService.getJourneyDetails(new Station("BYM", "Burnley Manchester Road", 0, 0), null, 0).getBody();
        assertThat(errorMessage).isEqualTo("Request Error! Please report to Team Beach.");
        errorMessage = (String) scheduledJourneyService.getJourneyDetails(null, new Station("BYM", "Burnley Manchester Road", 0, 0), 0).getBody();
        assertThat(errorMessage).isEqualTo("Request Error! Please report to Team Beach.");
    }

    @Test
    public void shouldConvertJSONResponseToJourney() throws IOException {
        String JSON = new String(Files.readAllBytes(Paths.get("./src/test/mocks/ScheduledJourneyResponse.json")));
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "", "15:38", "-1:58", "16:26", "16:26", true);
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.jsonResponseAsJourney(JSON, 0);
        assertThat(actualScheduledJourney).isEqualTo(expectedScheduledJourney);
    }

    @Test
    public void shouldReturnSecondDepartureWhenRequested() throws IOException {
        String JSON = new String(Files.readAllBytes(Paths.get("./src/test/mocks/ScheduledJourneyResponse.json")));
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "2", "16:36", "16:36", "17:24", "17:24", false);
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
        ScheduledJourney expectedScheduledJourney = new ScheduledJourney(new Station("BYM", "Burnley Manchester Road", 0, 0), new Station("MCV", "Manchester Victoria", 0, 0), "", "15:38", "-1:58", "16:26", "16:26", true);
        ScheduledJourney actualScheduledJourney = scheduledJourneyService.jsonResponseAsJourney(JSON);
        assertThat(actualScheduledJourney).isEqualTo(expectedScheduledJourney);
    }

    @Test
    public void getAllJourneyDetailsShouldReturnAnArrayOfJourneys() throws IOException {
        String mockResponseBody = new String(Files.readAllBytes(Paths.get("./src/test/mocks/MultipleScheduledJourneysResponse.json")));
        when(restTemplate.getForEntity("https://beach-train-ldb.herokuapp.com/journeys/MCV/LDS", String.class)).thenReturn(new ResponseEntity<>(mockResponseBody, HttpStatus.OK));

        ScheduledJourney expectedScheduledJourney1 = new ScheduledJourney(new Station("MCV", "Manchester Victoria", 0, 0), new Station("LDS", "Leeds", 0, 0), "4", "13:15", "13:15", "14:10", "14:10", false);
        ScheduledJourney expectedScheduledJourney2 = new ScheduledJourney(new Station("MCV", "Manchester Victoria", 0, 0), new Station("LDS", "Leeds", 0, 0), "6", "13:20", "13:20", "14:42", "14:42", false);
        ScheduledJourney expectedScheduledJourney3 = new ScheduledJourney(new Station("MCV", "Manchester Victoria", 0, 0), new Station("LDS", "Leeds", 0, 0), "4", "13:30", "13:30", "14:21", "14:21", false);
        ScheduledJourney expectedScheduledJourney4 = new ScheduledJourney(new Station("MCV", "Manchester Victoria", 0, 0), new Station("LDS", "Leeds", 0, 0), "6", "13:37", "13:37", "15:03", "15:03", false);

        ScheduledJourney[] expectedResult = {expectedScheduledJourney1, expectedScheduledJourney2, expectedScheduledJourney3, expectedScheduledJourney4};

        ResponseEntity actualScheduledJourney = scheduledJourneyService.getAllJourneyDetails(new Station("MCV", "Manchester Victoria", 0, 0), new Station("LDS", "Leeds", 0, 0));

        assertThat(actualScheduledJourney.getBody()).isEqualTo(expectedResult);
    }

    @Test
    public void getAllJourneyDetailsShouldThrowAnErrorWhenNullPassedIn() throws IOException {
        ResponseEntity actualScheduledJourney = scheduledJourneyService.getAllJourneyDetails(null, new Station("LDS", "Leeds", 0, 0));
        assertThat(actualScheduledJourney.getStatusCodeValue()).isEqualTo(500);
    }

    @Test
    public void getAllJourneyDetailsShouldReturnAnErrorOnErrorFromDownstream() throws IOException {
        when(restTemplate.getForEntity("https://beach-train-ldb.herokuapp.com/journeys/MCV/LDS", String.class)).thenReturn(new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity actualScheduledJourney = scheduledJourneyService.getAllJourneyDetails(new Station("MCV", "Manchester Victoria", 0, 0), new Station("LDS", "Leeds", 0, 0));
        assertThat(actualScheduledJourney.getStatusCodeValue()).isEqualTo(500);
    }

    @Test
    public void getAllJourneyDetailsShouldReturnAnErrorWhenNoDepartures() throws IOException {
        String mockNoTrainsResponse = "{\"departures\":[]}";
        when(restTemplate.getForEntity("https://beach-train-ldb.herokuapp.com/journeys/MCV/LDS", String.class)).thenReturn(new ResponseEntity<>(mockNoTrainsResponse, HttpStatus.OK));

        ResponseEntity actualScheduledJourney = scheduledJourneyService.getAllJourneyDetails(new Station("MCV", "Manchester Victoria", 0, 0), new Station("LDS", "Leeds", 0, 0));
        assertThat(actualScheduledJourney.getStatusCodeValue()).isEqualTo(500);
    }
}