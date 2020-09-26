package com.pt.learn.tddexamples.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PublishApiConfig.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class PublishApiConfigTest {

    @Autowired
    private PublishApiConfig publishApiConfig;

    @Test
    void succeedingTest() {

    }

}
