package com.aierview.backend.interview.infra.adapter.publisher;

import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventPublisher;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.infra.dto.NextQuestionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaInterviewEventPublisherAdapter implements IInterviewEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Question question) {
        NextQuestionEvent payload = new NextQuestionEvent(question.getInterview().getId(), question.getId(), question.getQuestion());
        kafkaTemplate.send("interview.next-question-text", payload);
    }
}
