package com.aierview.backend.interview.infra.controller;

import com.aierview.backend.interview.domain.model.OnAnswerReceivedRequest;
import com.aierview.backend.interview.domain.model.OnQuestionReceivedRequest;
import com.aierview.backend.interview.usecase.contract.IOnAnswerReceived;
import com.aierview.backend.interview.usecase.contract.IOnQuestionReceived;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class InterviewWebSocketController {
    private final IOnQuestionReceived onQuestionReceivedUseCase;
    private final IOnAnswerReceived onAnswerReceivedUseCase;

    @MessageMapping("/question/received")
    public void onQuestionReceived(@Payload OnQuestionReceivedRequest request) {
        onQuestionReceivedUseCase.execute(request);
    }

    @MessageMapping("/question/answered")
    public void onAnswerReceived(@Payload OnAnswerReceivedRequest request) {
        System.out.println(request.toString());
        this.onAnswerReceivedUseCase.execute(request);
    }
}
