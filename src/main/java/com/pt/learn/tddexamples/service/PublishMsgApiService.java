package com.pt.learn.tddexamples.service;

import com.pt.learn.tddexamples.apidata.DisplayMessage;
import com.pt.learn.tddexamples.request.DisplayMsgRequest;

public interface PublishMsgApiService {

    public DisplayMessage publishNewMsg(DisplayMsgRequest displayMsgRequest);
}
