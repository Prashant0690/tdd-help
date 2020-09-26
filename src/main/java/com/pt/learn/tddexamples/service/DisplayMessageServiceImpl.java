package com.pt.learn.tddexamples.service;

import com.pt.learn.tddexamples.apidata.DisplayMessage;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisplayMessageServiceImpl implements DisplayMessageService {

    @Autowired
    private PublishMsgApiService publishMsgApiService;

    @Override
    public DisplayMessage newDisplayMessage(DisplayMsgRequest request) {
        return publishMsgApiService.publishNewMsg(request);
    }
}
