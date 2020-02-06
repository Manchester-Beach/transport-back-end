package com.manchesterbeach.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manchesterbeach.transport.domain.ScheduledJourney;
import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.service.ScheduledJourneyService;
import com.manchesterbeach.transport.service.StationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduledJourneyController.class)
public class ScheduledJourneyControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StationService stationService;

    @MockBean
    ScheduledJourneyService scheduledJourneyService;

    @Test
    public void shouldReturnJourneyDetailsFromRequest() throws Exception {
        // given
        String uri = "/scheduledJourneys/MCV/BYM/0";
        Station departureStation = new Station("MCV", "Manchester Victoria", 0, 0);
        Station arrivalStation = new Station("BYM", "Burnley Manchester Road", 0, 0);
        ScheduledJourney scheduledJourney = new ScheduledJourney(departureStation, arrivalStation, "17:04", "17:24", "6", "17:51", false);
        when(scheduledJourneyService.getJourneyDetails(departureStation, arrivalStation, 0)).thenReturn(new ResponseEntity(scheduledJourney, HttpStatus.OK));
        when(stationService.getOneStation("MCV")).thenReturn(departureStation);
        when(stationService.getOneStation("BYM")).thenReturn(arrivalStation);
        String journeyJSON = objectMapper.writeValueAsString(scheduledJourney);
        // when
        String response = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        // then
        verify(scheduledJourneyService).getJourneyDetails(departureStation, arrivalStation, 0);
        verifyNoMoreInteractions(scheduledJourneyService);
        assertThat(response).isEqualTo(journeyJSON);
    }

    @Test
    public void shouldReturnEmptyResponseWhenGetJourneyDetailsReturnsNull() throws Exception {
        // given
        String uri = "/scheduledJourneys/MCV/BYM/0";
        Station departureStation = new Station("MCV", "Manchester Victoria", 0, 0);
        Station arrivalStation = new Station("BYM", "Burnley Manchester Road", 0, 0);
        when(scheduledJourneyService.getJourneyDetails(departureStation, arrivalStation, 0)).thenReturn(null);
        when(stationService.getOneStation("MCV")).thenReturn(departureStation);
        when(stationService.getOneStation("BYM")).thenReturn(arrivalStation);
        // when
        String response = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        // then
        verify(scheduledJourneyService).getJourneyDetails(departureStation, arrivalStation, 0);
        verifyNoMoreInteractions(scheduledJourneyService);
        assertThat(response).isEqualTo("");
    }

    @Test
    public void shouldReturnNullForInvalidStations() throws Exception {
        // given
        String uri = "/scheduledJourneys/MCV/BYM/0";
        Station departureStation = null;
        Station arrivalStation = new Station("BYM", "Burnley Manchester Road", 0, 0);
        when(stationService.getOneStation("MCV")).thenReturn(departureStation);
        when(stationService.getOneStation("BYM")).thenReturn(arrivalStation);
        // when
        String response = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        // then
        verify(scheduledJourneyService).getJourneyDetails(departureStation, arrivalStation, 0);
        verifyNoMoreInteractions(scheduledJourneyService);
        assertThat(response).isEqualTo("");
    }

    @Test
    public void shouldDefaultToFirstJourneyIfIndexNotSpecified() throws Exception {
        // given
        String uri = "/scheduledJourneys/MCV/BYM";
        Station departureStation = new Station("MCV", "Manchester Victoria", 0, 0);
        Station arrivalStation = new Station("BYM", "Burnley Manchester Road", 0, 0);
        ScheduledJourney scheduledJourney = new ScheduledJourney(departureStation, arrivalStation, "17:04", "17:24", "6", "17:51", false);
        when(scheduledJourneyService.getJourneyDetails(departureStation, arrivalStation, 0)).thenReturn(new ResponseEntity(scheduledJourney, HttpStatus.OK));
        when(stationService.getOneStation("MCV")).thenReturn(departureStation);
        when(stationService.getOneStation("BYM")).thenReturn(arrivalStation);
        String journeyJSON = objectMapper.writeValueAsString(scheduledJourney);
        // when
        String response = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        // then
        verify(scheduledJourneyService).getJourneyDetails(departureStation, arrivalStation, 0);
        verifyNoMoreInteractions(scheduledJourneyService);
        assertThat(response).isEqualTo(journeyJSON);
    }

}
