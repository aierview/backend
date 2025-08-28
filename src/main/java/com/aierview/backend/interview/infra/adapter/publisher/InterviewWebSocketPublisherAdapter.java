package com.aierview.backend.interview.infra.adapter.publisher;

import com.aierview.backend.interview.domain.contract.publisher.IInterviewWebSocketPublisher;
import com.aierview.backend.interview.domain.model.CurrentQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterviewWebSocketPublisherAdapter implements IInterviewWebSocketPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void execute(Long interviewId, CurrentQuestion currentQuestion) {
        messagingTemplate.convertAndSend("/topic/interview/" + interviewId, currentQuestion);
    }
}
