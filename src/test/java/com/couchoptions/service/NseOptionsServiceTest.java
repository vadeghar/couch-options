package com.couchoptions.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class NseOptionsServiceTest {

    @Autowired
    NseOptionsService nseOptionsService;

    @Test
    public void saveOptionsDataTest() {
        nseOptionsService.saveNiftyOptionsChain();
    }
}
