package com.manchesterbeach.transport.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JourneyTest {

    @Test
    public void journeyShouldHaveProperties(){
        //given
        String expectedName = "John Smith";
        Station expectedOriginStation = new Station("MAN", "Manchester Piccadilly");
        Station expectedDestinationStation = new Station("LIV", "Liverpool Lime Street");

        //when
        Journey journey = new Journey("John Smith",
                new Station("MAN", "Manchester Piccadilly"),
                new Station("LIV", "Liverpool Lime Street"));

        //then
        assertThat(journey.getName()).isEqualTo(expectedName);
        assertThat(journey.getOriginStation()).isEqualTo(expectedOriginStation);
        assertThat(journey.getDestinationStation()).isEqualTo(expectedDestinationStation);
    }

}