package com.aierview.backend.interview.infra.adapter.publisher;

import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventConsumer;
import com.aierview.backend.interview.domain.model.CurrentQuestion;
import com.aierview.backend.interview.usecase.contract.ISendCurrentQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterviewEventConsumerAdapter implements IInterviewEventConsumer {
    private final ISendCurrentQuestion sendCurrentQuestion;

    @Override
    @KafkaListener(topics = "interview.next-question-audio", groupId = "backend-service-group")
    public void consume(CurrentQuestion message) {
        this.sendCurrentQuestion.execute(message);
    }
}
