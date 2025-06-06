package com.aierview.infra.helper;

import com.aierview.domain.entity.Answer;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaFeedbackProducer {
    private final KafkaTemplate<String, Answer> kafkaTemplate;

    public void send(Answer answer) {
        kafkaTemplate.send("generate-feedback-topic", answer);
    }
}

