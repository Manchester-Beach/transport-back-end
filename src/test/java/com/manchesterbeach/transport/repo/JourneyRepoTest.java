package com.manchesterbeach.transport.repo;

import com.manchesterbeach.transport.domain.Journey;
import com.manchesterbeach.transport.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class JourneyRepoTest {

    private List<Journey> journeyList;

    @Mock
    private JourneyRepo journeyRepo;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        journeyList = new ArrayList<>(Arrays.asList(
                new Journey(new Station("MAN", "Manchester Piccadilly"), new Station("LIV", "Liverpool Lime Street")),
                new Journey(new Station("MAN", "Manchester Piccadilly"), new Station("LDS", "Leeds")),
                new Journey(new Station("LIV", "Liverpool Lime Street"), new Station("EDG", "Edge Hill"))
        ));
    }

    @Test
    public void countShouldReturnCorrectValue(){
        when(journeyRepo.getJourneyListSize()).thenReturn(journeyList.size());

        int expectedCount = 3;

        assertThat(journeyRepo.getJourneyListSize()).isEqualTo(expectedCount);
    }

}