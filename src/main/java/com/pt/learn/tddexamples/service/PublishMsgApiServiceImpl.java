package com.pt.learn.tddexamples.service;

import com.pt.learn.tddexamples.apidata.DisplayMessage;
import com.pt.learn.tddexamples.config.PublishApiConfig;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class PublishMsgApiServiceImpl implements PublishMsgApiService {

    @Autowired
    private PublishMsgTokenApi publishMsgTokenApi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PublishApiConfig publishApiConfig;

    @Override
    public DisplayMessage publishNewMsg(DisplayMsgRequest displayMsgRequest) {
        DisplayMessage displayMessage = null;
        try {
            displayMessage = callApi(displayMsgRequest, DisplayMessage.class, publishApiConfig.getPublishApiUrl(), HttpMethod.POST);
        }catch (RestClientResponseException clientResponseException){
            if(clientResponseException.getRawStatusCode() == 401){
                publishMsgTokenApi.updateToken();
                displayMessage = callApi(displayMsgRequest, DisplayMessage.class, publishApiConfig.getPublishApiUrl(), HttpMethod.POST);
            }else{
                throw clientResponseException;
            }

        }
        return displayMessage;
    }

    private <U, T> T callApi(U u, Class<T> type, String url, HttpMethod httpMethod){
        ResponseEntity<T> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication", "Bearer " + publishMsgTokenApi.getAccessToken());
        HttpEntity<U> requestEntity = new HttpEntity<>(u, headers);
        responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, type);
        return responseEntity.getBody();
    }
}
