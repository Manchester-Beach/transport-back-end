package com.manchesterbeach.transport.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class JourneyServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JourneyService journeyService;


}
