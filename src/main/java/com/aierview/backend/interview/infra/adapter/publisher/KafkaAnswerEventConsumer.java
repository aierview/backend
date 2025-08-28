package com.aierview.backend.interview.infra.adapter.publisher;

import com.aierview.backend.interview.domain.contract.IA.IIAGenerateFeedback;
import com.aierview.backend.interview.domain.contract.publisher.IAnswerEventConsumer;
import com.aierview.backend.interview.domain.contract.publisher.IAnswerEventPublisher;
import com.aierview.backend.interview.domain.model.AnswerEventConsumerPayload;
import com.aierview.backend.interview.domain.model.AnswerEventPublisherPayload;
import com.aierview.backend.interview.usecase.contract.IGenerateFeedback;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaAnswerEventConsumer implements IAnswerEventConsumer {
    private final IGenerateFeedback generateFeedback;

    @Override
    @KafkaListener(topics = "interview-answer.text", groupId = "backend-service-group")
    public void consume(AnswerEventConsumerPayload payload) {
        this.generateFeedback.execute(payload);
    }
}
