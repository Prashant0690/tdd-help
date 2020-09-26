package com.pt.learn.tddexamples;

import com.pt.learn.tddexamples.config.PublishApiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TddExamplesApplicationTests {

    @Autowired
    PublishApiConfig publishApiConfig;

    @Test
    void contextLoads() {
    }

}
