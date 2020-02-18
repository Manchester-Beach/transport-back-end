package com.manchesterbeach.transport.controller;

import com.manchesterbeach.transport.service.DeparturesService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeparturesControllerTest {
    @InjectMocks
    private DeparturesController departuresController;
    @Mock
    private DeparturesService departuresService;
    // TODO: Valid 'stationIdentifier', valid 'identifier'. – Return 'OK' with departures.
    // TODO: Invalid 'stationIdentifier', valid 'identifier'. –
    // TODO: Valid 'stationIdentifier', invalid 'identifier'.
    // TODO: Invalid 'stationIdentifier', invalid 'identifier'.
}
