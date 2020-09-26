package com.pt.learn.tddexamples.integrationTest;

import com.pt.learn.tddexamples.apidata.DisplayMessage;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DisplayMessageIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void postDisplayMessage() {
        String message = "What does Peter Parker say when people ask him what he does for a living? Web designer.";
        String from = "Flash";
        String to = "SpiderMan";
        /*int id = 100;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();*/
        DisplayMsgRequest msgRequest = new DisplayMsgRequest(message, from, to);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DisplayMsgRequest> request = new HttpEntity<>(msgRequest, headers);

        ResponseEntity<DisplayMessage> response = restTemplate
                .postForEntity("/display-message", request, DisplayMessage.class);
        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
        assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(from, response.getBody().getFrom());
        assertEquals(to, response.getBody().getTo());
        //assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getDate());
        assertNotNull(response.getBody().getTime());

    }

}
