package com.pt.learn.tddexamples.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pt.learn.tddexamples.apidata.DisplayMessage;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import com.pt.learn.tddexamples.service.DisplayMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@SpringBootTest*/
@WebMvcTest(DisplayMsgController.class)
public class DisplayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DisplayMessageService displayMessageService;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_postDisplayMessage() throws Exception {
        //Assemble
        String message = "What does Peter Parker say when people ask him what he does for a living? Web designer.";
        String from = "Flash";
        String to = "SpiderMan";
        int id = 100;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DisplayMsgRequest msgRequest = new DisplayMsgRequest(message, from, to);
        DisplayMessage displayMessage =
                new DisplayMessage(id, message, from, to, date.toString(), time.toString());
        when(displayMessageService.newDisplayMessage(eq(msgRequest))).thenReturn(displayMessage);
        //Act --> Assert
        mockMvc.perform(MockMvcRequestBuilders
                .post("/display-message")
                .content(asJsonString(msgRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.from", is(from)))
                .andExpect(jsonPath("$.to", is(to)))
                .andExpect(jsonPath("$.date", is(date.toString())))
                .andExpect(jsonPath("$.time", is(time.toString())));

        verify(displayMessageService, times(1)).newDisplayMessage(msgRequest);
    }
}
