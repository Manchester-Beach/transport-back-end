package com.manchesterbeach.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.service.StationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StationController.class)
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;

    @Test
    public void shouldGetAllStations() throws Exception {
        //given
        String uri = "/stations";
        List<Station> expectedStations = new ArrayList<Station>(
            Arrays.asList(
                new Station("MAN", "Manchester Piccadilly", 0, 0),
                new Station("LIV", "Liverpool Lime Street", 0, 0),
                new Station("LDS", "Leeds", 0, 0)
            ));

        String jsonStationList = objectMapper.writeValueAsString(expectedStations);

        when(stationService.getAllStations()).thenReturn(expectedStations);

        //when
        String response = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        assertThat(response).contains(jsonStationList);
        verify(stationService).getAllStations();
        verifyNoMoreInteractions(stationService);
    }

    @Test
    public void shouldReturnEmptyResponseWhenNoStations() throws Exception {
        String uri = "/stations";
        String jsonEmptyList = "{}";
        when(stationService.getAllStations()).thenReturn(null);

        //when
        String response = mockMvc.perform(get(uri)).andExpect(status().isNoContent()).andReturn().getResponse().getContentAsString();

        //then
        assertThat(response).isEqualTo(jsonEmptyList);
    }

    @Test
    public void shouldReturnOneStationWhenGivenId() throws Exception {
        //given
        String stationId = "MAN";
        String stationName = "Manchester Piccadilly";
        Station expectedStation = new Station(stationId, stationName, 0, 0);
        String stationAsString = objectMapper.writeValueAsString(expectedStation);
        String uri = String.format("/stations/%s", stationId);

        when(stationService.getOneStation(stationId)).thenReturn(expectedStation);

        //when
        String response = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        //then
        assertThat(response).contains(stationAsString);
    }

    @Test
    public void shouldReturnErrorWhenIncorrectIdGiven() throws Exception {
        //given
        String stationId = "!!!";
        String uri = String.format("/stations/%s", stationId);
        when(stationService.getOneStation(stationId)).thenReturn(null);

        //when
        //then
        String response = mockMvc.perform(get(uri)).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();


    }

}