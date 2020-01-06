package com.manchesterbeach.transport.service;

import com.manchesterbeach.transport.domain.Journey;
import com.manchesterbeach.transport.repo.JourneyRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JourneyServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JourneyRepo journeyRepo;

    @InjectMocks
    private JourneyService journeyService;

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
        int journeyIndex = 0;
        Journey journeyToDelete = new Journey();

        when(journeyRepo.getJourneyByIndex(journeyIndex)).thenReturn(journeyToDelete);
        when(journeyRepo.deleteJourney(journeyToDelete)).thenReturn(ResponseEntity.ok().build());

        //when
        ResponseEntity response = journeyService.deleteJourney(journeyIndex);

        //then
        verify(journeyRepo).deleteJourney(journeyToDelete);
    }
}
