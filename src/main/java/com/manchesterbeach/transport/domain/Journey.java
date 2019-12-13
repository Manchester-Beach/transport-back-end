package com.manchesterbeach.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Journey {

    private Station originStation;
    private Station destinationStation;

}
