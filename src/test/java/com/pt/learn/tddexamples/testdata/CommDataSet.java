package com.pt.learn.tddexamples.testdata;

import com.pt.learn.tddexamples.apidata.TokenRequest;
import com.pt.learn.tddexamples.apidata.TokenResponse;
import com.pt.learn.tddexamples.config.PublishApiConfig;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

public interface CommDataSet {

    int ID = 100;
    String MSG = "Hello mock message!!!";
    String FROM = "ABC";
    String TO = "XYZ";
    LocalDate DATE_NOW = LocalDate.now();
    LocalTime TIME_NOW = LocalTime.now();
    String DATE_STR = DATE_NOW.toString();
    String TIME_STR = TIME_NOW.toString();

    String ACCESS_TOKEN = "icG3JUrpyX79IrCf";
    String TOKEN_TYPE = "bearer";
    String EXPIRES_IN = "7200";

    String UNAUTHORIZED_MSG ="Authentication credentials were missing or incorrect.";
    int UNAUTHORIZED_STATUS_CODE = 401;
    String UNAUTHORIZED_STATUS = "401 Unauthorized";
    String UNAUTHORIZED_STATUS_TXT = "401 Unauthorized";

    static  HttpHeaders getUnauthorizedHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("status", UNAUTHORIZED_STATUS);
        return headers;
    }


    //Token Api methods
    static HttpEntity<TokenRequest> getTokenRequestHttpEntity(PublishApiConfig apiConfig) {
        HttpHeaders tokenHeaders = getHttpTokenHeaders(apiConfig);
        TokenRequest tokenRequest = getTokenRequest(apiConfig);
        HttpEntity<TokenRequest> entity = new HttpEntity<>(tokenRequest, tokenHeaders);
        return entity;
    }

    static HttpHeaders getHttpTokenHeaders(PublishApiConfig apiConfig) {
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
        tokenHeaders.add("client_secret", apiConfig.getTokenAccessSecret());
        return tokenHeaders;
    }

    static TokenRequest getTokenRequest(PublishApiConfig apiConfig) {
        return new TokenRequest(apiConfig.getTokenClientId(), apiConfig.getGrantType());
    }

    static TokenResponse getTokenResponse() {
        return new TokenResponse(ACCESS_TOKEN, TOKEN_TYPE, EXPIRES_IN);
    }

    static ResponseEntity<TokenResponse> getTokenResponseResponseEntity() {
        TokenResponse tokenResponse = getTokenResponse();
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    // Publish Msg api

    static DisplayMsgRequest getDisplayMsgRequest() {
        return new DisplayMsgRequest(MSG, FROM, TO);
    }

    static HttpHeaders getHttpHeadersPublishMsgApi() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authentication", "Bearer " + ACCESS_TOKEN);
        return headers;
    }

}
