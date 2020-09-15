package com.pt.learn.tddexamples.controller;

import com.pt.learn.tddexamples.dto.DiaplayMessage;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/display-message")
public class DisplayMsgController {

    @PostMapping
    public ResponseEntity<DiaplayMessage> newDisplayMsg(@RequestBody DisplayMsgRequest displayMsgRequest){
        System.out.println("display-message");
        return ResponseEntity.ok().body(new DiaplayMessage());
    }
}
