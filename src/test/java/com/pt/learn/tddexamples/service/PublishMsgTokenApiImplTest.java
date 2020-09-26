package com.pt.learn.tddexamples.service;

import com.pt.learn.tddexamples.apidata.TokenRequest;
import com.pt.learn.tddexamples.apidata.TokenResponse;
import com.pt.learn.tddexamples.config.PublishApiConfig;
import com.pt.learn.tddexamples.testdata.CommDataSet;
import org.junit.jupiter.api.*;
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

import static com.pt.learn.tddexamples.testdata.CommDataSet.getTokenRequestHttpEntity;
import static com.pt.learn.tddexamples.testdata.CommDataSet.getTokenResponseResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PublishMsgTokenApiImpl.class,
        PublishApiConfig.class,
        PublishMsgTokenApiImplTest.MockBeanConfiguration.class
})
@TestPropertySource(locations = "classpath:test-application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublishMsgTokenApiImplTest implements CommDataSet {

    private static int INNVOCATION_COUNT = 0;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PublishApiConfig publishApiConfig;
    @Autowired
    private PublishMsgTokenApi publishMsgTokenApi;

    @BeforeEach
    void init() {
        INNVOCATION_COUNT = 0;
    }

    @AfterEach
    void tearDown() {
        clearInvocations(restTemplate);
    }

    @Test
    @Order(1)
    void test_init_method_loadToken() {
        assertEquals(ACCESS_TOKEN, publishMsgTokenApi.getAccessToken());
        verify(restTemplate, times(1))
                .exchange(eq(publishApiConfig.getTokenUrl()), eq(HttpMethod.POST), any(), eq(TokenResponse.class));
    }

    @Test
    void test_update_method_Success() {
        //Assemble
        HttpEntity<TokenRequest> entity = getTokenRequestHttpEntity(publishApiConfig);
        TokenResponse tokenRequestNew = new TokenResponse("new-access-token", TOKEN_TYPE, EXPIRES_IN);
        ResponseEntity<TokenResponse> responseEntity = new ResponseEntity<>(tokenRequestNew, HttpStatus.OK);
        when(restTemplate.exchange(eq(publishApiConfig.getTokenUrl()), eq(HttpMethod.POST), eq(entity), eq(TokenResponse.class)))
                .thenReturn(responseEntity);
        //ACT
        publishMsgTokenApi.updateToken();
        //Assert
        assertEquals("new-access-token", publishMsgTokenApi.getAccessToken());
        verify(restTemplate, times(1))
                .exchange(eq(publishApiConfig.getTokenUrl()), eq(HttpMethod.POST), any(), eq(TokenResponse.class));
    }

    /*Executing same mock method multiple time on a same test with different response */
    @Test
    void test_update_method_Success_With_Retry() {
        //Assemble
        HttpEntity<TokenRequest> entity = getTokenRequestHttpEntity(publishApiConfig);
        TokenResponse tokenRequestNew = new TokenResponse("new-access-token-retry", TOKEN_TYPE, EXPIRES_IN);
        ResponseEntity<TokenResponse> responseEntity = new ResponseEntity<>(tokenRequestNew, HttpStatus.OK);
        when(restTemplate.exchange(eq(publishApiConfig.getTokenUrl()), eq(HttpMethod.POST), eq(entity), eq(TokenResponse.class)))
                .thenAnswer(invocationOnMock -> {
                    if (INNVOCATION_COUNT == 0 || INNVOCATION_COUNT == 1) {
                        INNVOCATION_COUNT++;
                        HttpHeaders headers = new HttpHeaders();
                        throw new RestClientResponseException("Rest exp", 403, "Forbidden", headers, null, null);
                    }
                    return responseEntity;
                });
        //ACT
        publishMsgTokenApi.updateToken();
        //Assert
        assertEquals("new-access-token-retry", publishMsgTokenApi.getAccessToken());
        verify(restTemplate, times(3))
                .exchange(eq(publishApiConfig.getTokenUrl()), eq(HttpMethod.POST), any(), eq(TokenResponse.class));
    }

    /*Configuring mock bean with a mock method call for testing init/lifecycle methods  */
    @Configuration
    public static class MockBeanConfiguration implements CommDataSet {
        @Autowired
        private PublishApiConfig publishApiConfig;

        @Bean
        public RestTemplate createRestTemplate() {
            RestTemplate restTemplate = mock(RestTemplate.class);
            HttpEntity<TokenRequest> entity = getTokenRequestHttpEntity(publishApiConfig);
            ResponseEntity<TokenResponse> responseEntity = getTokenResponseResponseEntity();
            when(restTemplate.exchange(eq(publishApiConfig.getTokenUrl()), eq(HttpMethod.POST), eq(entity), eq(TokenResponse.class)))
                    .thenReturn(responseEntity);
            return restTemplate;
        }

    }

}
