package com.pt.learn.tddexamples.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/*@SpringBootTest*/
@WebMvcTest(DisplayMsgController.class)
public class DisplayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_postDisplayMessage() throws Exception {
        String message = "What does Peter Parker say when people ask him what he does for a living? Web designer.";
        String from = "Flash";
        String to = "SpiderMan";
        DisplayMsgRequest msgRequest = new DisplayMsgRequest(message, from, to);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/display-message")
                .content(asJsonString(msgRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
