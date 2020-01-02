package com.manchesterbeach.transport.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StationTest {

    @Test
    public void stationShouldHaveProperties(){
        //given
        String expectedId = "MAN";
        String expectedName = "Manchester Piccadilly";

        //when
        Station station = new Station("MAN", "Manchester Piccadilly", 0, 0);

        //then
        assertThat(station.getId()).isEqualTo(expectedId);
        assertThat(station.getName()).isEqualTo(expectedName);
    }
}