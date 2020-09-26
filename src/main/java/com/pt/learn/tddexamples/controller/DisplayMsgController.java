package com.pt.learn.tddexamples.controller;

import com.pt.learn.tddexamples.apidata.DisplayMessage;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import com.pt.learn.tddexamples.service.DisplayMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/display-message")
public class DisplayMsgController {

    @Autowired
    private DisplayMessageService displayMsgService;

    @PostMapping
    public ResponseEntity<DisplayMessage> addDisplayMsg(@RequestBody DisplayMsgRequest displayMsgRequest) {
        System.out.println("display-message");
        return ResponseEntity
                .ok()
                .body(displayMsgService.newDisplayMessage(displayMsgRequest));
    }

}
