package com.pt.learn.tddexamples.service;

import com.pt.learn.tddexamples.apidata.DisplayMessage;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

/*@ExtendWith(SpringExtension.class)
@ContextConfiguration()*/
@ExtendWith(MockitoExtension.class)
public class DisplayMessageServiceImplTest {

    @InjectMocks
    private final DisplayMessageService displayMessageService = new DisplayMessageServiceImpl();

    @Mock
    private PublishMsgApiService publishMsgApiService;

    @Test
    public void test_NewDisplayMessage_Success() {
        //Assemble
        int id = 100;
        String msg = "Hello mock message!!!";
        String from = "ABC";
        String to = "XYZ";
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DisplayMessage displayMessage =
                new DisplayMessage(id, msg, from, to, date.toString(), time.toString());

        DisplayMsgRequest displayMsgRequest = new DisplayMsgRequest(msg, from, to);
        when(publishMsgApiService.publishNewMsg(eq(displayMsgRequest))).thenReturn(displayMessage);
        //Act

        DisplayMessage response = displayMessageService.newDisplayMessage(displayMsgRequest);
        //Assert
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(msg, response.getMessage());
        assertEquals(from, response.getFrom());
        assertEquals(to, response.getTo());
        assertEquals(date.toString(), response.getDate());
        assertEquals(time.toString(), response.getTime());
    }

   /* @Test
    public void test_NewDisplayMessage_RuntimeExp(){
        //Assemble

        String msg = "Hello mock message!!!";
        String from = "ABC";
        String to = "XYZ";
        DisplayMsgRequest displayMsgRequest = new DisplayMsgRequest(msg, from, to);

        RuntimeException runtimeException = new RuntimeException("runtime exp");
        when(publishMsgApiService.publishNewMsg(eq(displayMsgRequest)))
                .thenThrow(runtimeException);
        //Act Assert
        RuntimeException expResponse = assertThrows(RuntimeException.class, ()->{
            displayMessageService.newDisplayMessage(displayMsgRequest);
        });



    }*/

}
