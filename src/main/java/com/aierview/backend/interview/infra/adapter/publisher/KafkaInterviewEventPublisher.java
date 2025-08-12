package com.aierview.backend.interview.infra.adapter.publisher;

import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventPublisher;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.infra.dto.FirstQuestionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaInterviewEventPublisher implements IInterviewEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishFirstQuestion(Question question) {
        FirstQuestionEvent payload = new FirstQuestionEvent(question.getInterview().getId(), question.getId(), question.getQuestion());
        kafkaTemplate.send("interview.next-question-text", payload);
    }
}
