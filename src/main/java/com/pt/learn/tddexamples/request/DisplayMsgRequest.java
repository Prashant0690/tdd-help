package com.pt.learn.tddexamples.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayMsgRequest {
    private String message;
    private String from;
    private String to;

}
