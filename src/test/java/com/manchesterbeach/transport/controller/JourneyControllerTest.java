package com.manchesterbeach.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manchesterbeach.transport.domain.Journey;
import com.manchesterbeach.transport.domain.Station;
import com.manchesterbeach.transport.service.JourneyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JourneyController.class)
public class JourneyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JourneyService journeyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldPostJourney() throws Exception {
        //given
        Station origin = new Station("MAN", "Manchester Piccadilly", 0, 0);
        Station destination = new Station("LIV", "Liverpool Lime Street", 0, 0);

        Journey expectedJourney = new Journey(origin, destination);

        //language=JSON
        String expectedJourneyJson = "{" +
                " \"origin\": \"MAN\"," +
                " " +
                "\"destination\": \"LIV\", \"lat\": 0, \"lon\": 0 " +
                "}";
        URI uri = new URI("/journeys");

        when(journeyService.saveJourney(origin.getId(), destination.getId())).thenReturn(ResponseEntity.created(uri).build());

        //when
        mockMvc.perform(post("/journeys").contentType(MediaType.APPLICATION_JSON)
                .content(expectedJourneyJson))
                .andExpect(status().isCreated());

        //then
        verify(journeyService).saveJourney(origin.getId(), destination.getId());
    }

    @Test
    public void shouldGetAllJourneys() throws Exception {
        //given
        String uri = "/journeys";

        ArrayList<Journey> journeys = new ArrayList<>(
                Arrays.asList(
                    new Journey(),
                    new Journey(),
                    new Journey()
                )
        );

        String jsonJourneyList = objectMapper.writeValueAsString(journeys);

        when(journeyService.getAllJourneys()).thenReturn(journeys);

        //when
        String response = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        assertThat(response).contains(jsonJourneyList);
    }

    @Test
    public void shouldDeleteJourneyOnRequest() throws Exception {
        //given
        int journeyIndex = 2;
        String uri = String.format("/journeys/%s", journeyIndex);

        ArrayList journeys = new ArrayList<>(
                Arrays.asList(
                        new Journey(),
                        new Journey(),
                        new Journey(),
                        new Journey()
                )
        );

        when(journeyService.deleteJourney(journeyIndex)).thenReturn(ResponseEntity.ok().build());

        //when
        mockMvc.perform(delete(uri)).andExpect(status().isOk());

        //then
        verify(journeyService).deleteJourney(journeyIndex);
        verifyNoMoreInteractions(journeyService);
    }
}