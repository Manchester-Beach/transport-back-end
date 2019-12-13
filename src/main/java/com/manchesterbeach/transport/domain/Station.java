package com.manchesterbeach.transport.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {

    @JsonProperty("crs")
    private String id;
    private String name;

    public Station(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(){

    }
}
