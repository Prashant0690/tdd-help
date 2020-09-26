package com.pt.learn.tddexamples.apidata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private String access_token;
    private String token_type;
    private String expires_in;

}
