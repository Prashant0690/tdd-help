package com.pt.learn.tddexamples.service;

import com.pt.learn.tddexamples.apidata.DisplayMessage;
import com.pt.learn.tddexamples.config.PublishApiConfig;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import static com.pt.learn.tddexamples.testdata.CommDataSet.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PublishMsgApiServiceImpl.class,
        PublishApiConfig.class,
        PublishMsgApiServiceImplTest.MockBeanConfiguration.class
})
@TestPropertySource(locations = "classpath:test-application.properties")
public class PublishMsgApiServiceImplTest {

    private static int INNVOCATION_COUNT = 0;
    @Autowired
    private PublishMsgApiService publishMsgApiService;
    @Autowired
    private PublishMsgTokenApi publishMsgTokenApi;
    @Autowired
    private PublishApiConfig publishApiConfig;
    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    void init() {
        INNVOCATION_COUNT = 0;
    }

    @AfterEach
    void tearDown() {
        clearInvocations(restTemplate, publishMsgTokenApi);
    }

    @Test
    void test_publishMessage_Success() {
        //Assemble
        HttpEntity<DisplayMsgRequest> entity = new HttpEntity<>(getDisplayMsgRequest(), getHttpHeadersPublishMsgApi());
        DisplayMessage displayMessage = new DisplayMessage(ID, MSG, FROM, TO, DATE_STR, TIME_STR);
        when(restTemplate.exchange(eq(publishApiConfig.getPublishApiUrl()),
                eq(HttpMethod.POST),
                eq(entity),
                eq(DisplayMessage.class)))
                .thenReturn(new ResponseEntity<>(displayMessage, HttpStatus.OK));
        when(publishMsgTokenApi.getAccessToken()).thenReturn(ACCESS_TOKEN);
        //Act
        DisplayMessage response = publishMsgApiService.publishNewMsg(getDisplayMsgRequest());
        //Assert
        verify(restTemplate, times(1))
                .exchange(eq(publishApiConfig.getPublishApiUrl()),
                        eq(HttpMethod.POST),
                        eq(entity),
                        eq(DisplayMessage.class));
        verify(publishMsgTokenApi, times(1)).getAccessToken();
        assertAll("Publish msg response success",
                () -> assertEquals(ID, response.getId()), () -> assertEquals(MSG, response.getMessage()),
                () -> assertEquals(FROM, response.getFrom()), () -> assertEquals(TO, response.getTo()),
                () -> assertEquals(DATE_STR, response.getDate()), () -> assertEquals(TIME_STR, response.getTime()));
    }

    @Test
    void test_publishMessage_return_401Unauthorized_UpdateToken_Success() {
        //Assemble
        HttpEntity<DisplayMsgRequest> entity = new HttpEntity<>(getDisplayMsgRequest(), getHttpHeadersPublishMsgApi());
        DisplayMessage displayMessage = new DisplayMessage(ID, MSG, FROM, TO, DATE_STR, TIME_STR);
        when(publishMsgTokenApi.getAccessToken()).thenReturn(ACCESS_TOKEN, ACCESS_TOKEN);
        when(restTemplate.exchange(eq(publishApiConfig.getPublishApiUrl()), eq(HttpMethod.POST), eq(entity), eq(DisplayMessage.class)))
                .thenAnswer(invocationOnMock -> {
                    if (INNVOCATION_COUNT == 0) {
                        INNVOCATION_COUNT++;
                        HttpHeaders headers = new HttpHeaders();
                        headers.set("status", "401 Unauthorized");
                        throw new RestClientResponseException("Unauthorized", 401, "401 Unauthorized", headers, null, null);
                    }else{
                        return new ResponseEntity<>(displayMessage, HttpStatus.OK);
                    }

                });
        //Act
        DisplayMessage response = publishMsgApiService.publishNewMsg(getDisplayMsgRequest());
        //Assert
        assertAll("Publish msg response assert 401 Unauthorized",
                () -> assertEquals(ID, response.getId()), () -> assertEquals(MSG, response.getMessage()),
                () -> assertEquals(FROM, response.getFrom()), () -> assertEquals(TO, response.getTo()),
                () -> assertEquals(DATE_STR, response.getDate()), () -> assertEquals(TIME_STR, response.getTime()));
        verify(publishMsgTokenApi, times(2)).getAccessToken();
        verify(publishMsgTokenApi, times(1)).updateToken();
        verify(restTemplate, times(2))
                .exchange(eq(publishApiConfig.getPublishApiUrl()), eq(HttpMethod.POST), eq(entity), eq(DisplayMessage.class));

    }

    //401	UNAUTHORIZED	Authentication credentials were missing or incorrect.
    @Test
    void test_publishMessage_return_401Unauthorized_UpdateToken_PublishMsg_re_return_401_Failure() {
        //Assemble
        HttpEntity<DisplayMsgRequest> entity = new HttpEntity<>(getDisplayMsgRequest(), getHttpHeadersPublishMsgApi());

        when(restTemplate.exchange(eq(publishApiConfig.getPublishApiUrl()), eq(HttpMethod.POST), eq(entity), eq(DisplayMessage.class)))
                .thenThrow(new RestClientResponseException(UNAUTHORIZED_MSG, UNAUTHORIZED_STATUS_CODE, UNAUTHORIZED_STATUS_TXT, getUnauthorizedHeader(), null, null));
        //Act
        RestClientResponseException response = assertThrows(RestClientResponseException.class,
                () -> publishMsgApiService.publishNewMsg(getDisplayMsgRequest()));
        //Assert
        assertEquals(UNAUTHORIZED_STATUS_CODE, response.getRawStatusCode());
        verify(restTemplate, times(2))
                .exchange(eq(publishApiConfig.getPublishApiUrl()),
                        eq(HttpMethod.POST),
                        eq(entity), eq(DisplayMessage.class));
        verify(publishMsgTokenApi, times(2)).getAccessToken();
        verify(publishMsgTokenApi, times(1)).updateToken();
    }

    @Test
    void test_publishMessage_return_Other_Exp_like_403_Except_401Unauthorized() {
        //Assemble
        HttpEntity<DisplayMsgRequest> entity = new HttpEntity<>(getDisplayMsgRequest(), getHttpHeadersPublishMsgApi());
        HttpHeaders headers = new HttpHeaders();
        headers.set("status", "403 Forbidden");
        when(restTemplate.exchange(eq(publishApiConfig.getPublishApiUrl()), eq(HttpMethod.POST), eq(entity), eq(DisplayMessage.class)))
                .thenThrow(new RestClientResponseException("Resource Forbidden", 403, "403 Forbidden", headers, null, null));
        //Act
        RestClientResponseException response = assertThrows(RestClientResponseException.class,
                () -> publishMsgApiService.publishNewMsg(getDisplayMsgRequest()));
        //Assert
        assertEquals(403, response.getRawStatusCode());
        verify(restTemplate, times(1))
                .exchange(eq(publishApiConfig.getPublishApiUrl()), eq(HttpMethod.POST), eq(entity), eq(DisplayMessage.class));
        verify(publishMsgTokenApi, times(1)).getAccessToken();
    }

    @Configuration
    public static class MockBeanConfiguration {
        @Bean
        public RestTemplate createRestTemplate() {
            return mock(RestTemplate.class);
        }

        @Bean
        public PublishMsgTokenApi createPublishMsgTokenApi() {
            return mock(PublishMsgTokenApi.class);
        }
    }

}
