package com.pt.learn.tddexamples.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

/*@Entity*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaplayMessage {
    @Id
    @GeneratedValue
    private int id;
    private String message;
    private String from;
    private String to;
    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;
    @Column(name = "time", columnDefinition = "TIME")
    private LocalTime time;
}
