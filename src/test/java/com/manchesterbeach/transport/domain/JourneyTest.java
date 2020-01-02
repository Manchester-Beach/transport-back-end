package com.manchesterbeach.transport.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JourneyTest {

    @Test
    public void journeyShouldHaveProperties(){
        //given
        String expectedName = "John Smith";
        Station expectedOriginStation = new Station("MAN", "Manchester Piccadilly", 0, 0);
        Station expectedDestinationStation = new Station("LIV", "Liverpool Lime Street", 0, 0);

        //when
        Journey journey = new Journey(
                new Station("MAN", "Manchester Piccadilly", 0, 0),
                new Station("LIV", "Liverpool Lime Street", 0, 0));

        //then
        assertThat(journey.getOriginStation()).isEqualTo(expectedOriginStation);
        assertThat(journey.getDestinationStation()).isEqualTo(expectedDestinationStation);
    }

}