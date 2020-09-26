package com.pt.learn.tddexamples.apidata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayMessage {

    private int id;
    private String message;
    private String from;
    private String to;
    private String date;
    private String time;
}
