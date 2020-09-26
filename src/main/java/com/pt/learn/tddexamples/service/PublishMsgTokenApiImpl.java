package com.pt.learn.tddexamples.service;

import com.pt.learn.tddexamples.apidata.TokenRequest;
import com.pt.learn.tddexamples.apidata.TokenResponse;
import com.pt.learn.tddexamples.config.PublishApiConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PublishMsgTokenApiImpl implements PublishMsgTokenApi, InitializingBean {

    TokenResponse tokenResponse;
    RetryTemplate retryTemplate = RetryTemplate.builder()
            .maxAttempts(3)
            .fixedBackoff(1000)
            .build();
    @Autowired
    private PublishApiConfig publishApiConfig;
    @Autowired
    private RestTemplate restTemplate;

    private void fetchNewToken() {
        tokenResponse = null;
        HttpHeaders tokenHeaders = new HttpHeaders();
        //tokenHeaders.setAccept(MediaType.APPLICATION_JSON_VALUE);
        tokenHeaders.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
        tokenHeaders.add("client_secret", publishApiConfig.getTokenAccessSecret());
        TokenRequest tokenRequest = new TokenRequest(publishApiConfig.getTokenClientId(), publishApiConfig.getGrantType());
        HttpEntity<TokenRequest> entity = new HttpEntity<>(tokenRequest, tokenHeaders);
        tokenResponse = retryTemplate.execute(retryContext -> {
            ResponseEntity<TokenResponse> responseEntity = restTemplate.exchange(publishApiConfig.getTokenUrl(), HttpMethod.POST, entity, TokenResponse.class);
            return responseEntity.getBody();
        });

    }

    public String getAccessToken() {
        return tokenResponse.getAccess_token();
    }

    public void updateToken() {
        fetchNewToken();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("*************WORKING*****************");
        fetchNewToken();
    }
}
